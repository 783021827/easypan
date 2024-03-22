package com.easypan.util;

import com.easypan.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class ProcessUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    public static String executeCommand(String cmd, Boolean outPrintLog) throws BusinessException {
        if (StringUtils.isEmpty(cmd)) {
            logger.error("指令执行失败，ffmpeg为空指令");
            return null;
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            //执行ffmpeg命令
            //取出输出流和错误流的信息
            //注意：必须要取出ffmpeg在执行任务过程中产生的输出信息，如果不取的话，当输出流信息填满jvm存储输出流信息的缓冲区时，线程就会阻塞住
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());

            errorStream.start();
            inputStream.start();
            // 等待ffmpeg命令执行完
            process.waitFor();
            //获取执行结果的字符串
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer + "\n").toString();

            if (outPrintLog) {
                logger.info("执行命令{},结果:{}", cmd, result);
            } else {
                logger.info("执行成功{}", cmd);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("视频转换失败");
        } finally {
            if (process != null) {
                ProcessKiller processKiller = new ProcessKiller(process);
                runtime.addShutdownHook(processKiller);
            }
        }
    }
    //在程序退出前结束线程

    private static class ProcessKiller extends Thread {
        private Process process;

        public ProcessKiller(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            this.process.destroy();
        }
    }

    // 用于取出ffmpeg线程执行过程中产生的各种输出和错误流信息
    static class PrintStream extends Thread {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        public PrintStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                if (inputStream == null) {
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line=bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }
            } catch (Exception e) {
                logger.error("读取输入流错了错误{}",e.getMessage());
            }finally {
                try {   
                    if(bufferedReader!=null){
                        bufferedReader.close();
                    }
                    if(inputStream!=null){
                        inputStream.close();
                    }
                }catch (Exception e){
                    logger.error("调用PrintStream关闭流失败");
                }
            }
        }
    }
}
