package mlbp.utils;

public class FilePathManager {

    public static String getFullPath(Prop prop, String key) {
        String filename = prop.getProperty(key);
        if (filename != null && filename.length() > 0) {
            String firstChar = String.valueOf(filename.charAt(0));
            if (firstChar.equals(prop.getProperty(Prop.fileSepKey))) {
                return filename;
            } else {
                filename = prop.getProperty(Prop.workDirKey) + prop.getProperty(Prop.fileSepKey) + filename;
                firstChar = String.valueOf(filename.charAt(0));
                if (firstChar.equals(prop.getProperty(Prop.fileSepKey))) {
                    return filename;
                } else {
                    filename = System.getProperty("user.dir") + prop.getProperty(Prop.fileSepKey) + filename;
                    return filename;
                }
            }
        } else {
            return null;
        }
    }
}
