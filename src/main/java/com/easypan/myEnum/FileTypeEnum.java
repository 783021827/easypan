package com.easypan.myEnum;

import org.apache.commons.lang3.ArrayUtils;

public enum FileTypeEnum {
    VIDEO(FileCategoryEnum.VIDEO, 1, new String[]{".mp4", ".avi", ".rmvb", ".mkv", ".mov"}, "视频"),
    MUSIC(FileCategoryEnum.MUSIC, 2, new String[]{".mp3", ".wav", ".wma", ".mp2", ".flac", ".midi", ".ra", ".ape", ".aac", ".cde",}, "音频"),
    IMAGE(FileCategoryEnum.IMAGE, 3, new String[]{".jpeg", ".jpg", ".png", ".gif", ".bmp", ".dds", ".psd", ".pdt", ".webp", ".xmp", ".svg"}, "图片"),
    PDF(FileCategoryEnum.DOC, 4, new String[]{".pdf"}, "pdf"),
    WORLD(FileCategoryEnum.DOC, 5, new String[]{".doc"}, "world"),
    EXCEL(FileCategoryEnum.DOC, 6, new String[]{".xlsx"}, "excel"),
    TXT(FileCategoryEnum.DOC, 7, new String[]{".txt"}, "txt"),
    PROGRAME(FileCategoryEnum.OTHERS, 8, new String[]{".h", ".c", ".cpp", ".hxx", ".cc", ".m", ".o", ".dll", ".cs", ".java", ".class", ".js", ".ts", ".css", ".scss", ".less", ".vue", ".jsx", "sql", ".md", ".json", ".html", ".xml"}, "code"),
    ZIP(FileCategoryEnum.OTHERS, 9, new String[]{".rar", ".zip", ".7z", ".tar", ".jar"}, "压缩包"),
    OTHERS(FileCategoryEnum.OTHERS, 10, new String[]{}, "其他");
    private FileCategoryEnum category;
    private Integer type;
    private String[] suffixs;
    private String desc;

    public FileCategoryEnum getCategory() {
        return category;
    }

    public Integer getType() {
        return type;
    }

    public String[] getSuffixs() {
        return suffixs;
    }

    public String getDesc() {
        return desc;
    }

    FileTypeEnum(FileCategoryEnum category, Integer type, String[] suffixs, String desc) {
        this.category = category;
        this.type = type;
        this.suffixs = suffixs;
        this.desc = desc;
    }

    public static FileTypeEnum getFileTypeBySuffix(String suffix) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
            if (ArrayUtils.contains(item.getSuffixs(), suffix)) {
                return item;
            }
        }
        return FileTypeEnum.OTHERS;
    }

    public static FileTypeEnum getFileTypeByType(Integer type) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return FileTypeEnum.OTHERS;
    }


}
