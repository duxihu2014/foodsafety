package com.otec.foodsafety.util;

public enum ResourceType {
	document("txt doc docx xls xlsx pdf", 1L), 
	image("jpg jpeg png gif", 2L), 
	video("mp4 rmvb avi mpg", 3L), 
	music("mp3", 4L), 
	media("swf flv", 5L), 
	compress("rar zip", 6L), 
	page("html", 7L), 
	other("?", 8L);

	private String suffix;
	private Long type;

	ResourceType(String suffix, Long type) {
		this.suffix = suffix;
		this.type = type;
	}

	public static Long getTypeBySuffix(String suffix) {
		if (suffix == null)
			return other.getType();
		String s = suffix + "";
		if (document.getSuffix().contains(s)) {
			return document.getType();
		} else if (image.getSuffix().contains(s)) {
			return image.getType();
		} else if (video.getSuffix().contains(s)) {
			return video.getType();
		} else if (music.getSuffix().contains(s)) {
			return music.getType();
		} else if (media.getSuffix().contains(s)) {
			return media.getType();
		} else if (compress.getSuffix().contains(s)) {
			return compress.getType();
		} else if (page.getSuffix().contains(s)) {
			return page.getType();
		} else {
			return other.getType();
		}
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}
