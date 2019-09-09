package mlbp.utils;

public class ReleaseFiltering {

    // Given the name of a release, the method returns its type
    public String extractTypeOfRelease(String pReleaseName) {
        String toReplace = "" + pReleaseName;

        int dotCount = toReplace.length() - toReplace.replace(".", "").length();
        int underscoreCount = toReplace.length() - toReplace.replace("_", "").length();

        if (pReleaseName.contains("RC")) {
            return "release-candidate";
        } else if (dotCount == 2) {
            String[] numbers = pReleaseName.split("\\.");
            int size = numbers.length;

            if (numbers[size - 1].equals("0")) { // x.x.0
                if (numbers[size - 2].equals("0")) { // x.0.0
                    return "major-release";
                } else {
                    return "minor-release";
                }
            } else {
                return "release-candidate";
            }
        } else if (dotCount == 1) {
            if (pReleaseName.endsWith("0")) {
                return "major-release";
            } else {
                return "minor-release";
            }
        } else if (underscoreCount == 2) {
            String[] numbers = pReleaseName.split("_");
            int size = numbers.length;

            if (numbers[size - 1].equals("0")) { // x_x_0
                if (numbers[size - 2].equals("0")) { // x_0_0
                    return "major-release";
                } else {
                    return "minor-release";
                }
            } else {
                return "release-candidate";
            }
        } else if (underscoreCount == 1) {
            if (countIntegers(pReleaseName) > 3) {
                return "release-candidate";
            } else if (pReleaseName.endsWith("0")) {
                return "major-release";
            } else {
                return "minor-release";
            }
        } else {
            return "release-candidate";
        }

        /*
         * if(pReleaseName.contains("RC")) return "release-candidate"; else if (dotCount
         * == 2) { if(pReleaseName.endsWith("0")) return "major-release"; else return
         * "patch"; } else if (dotCount == 1) { if(pReleaseName.endsWith("0")) return
         * "major-release"; else return "minor-release"; } else if (underscoreCount ==
         * 2) { if(pReleaseName.endsWith("0")) return "major-release"; else return
         * "patch"; } else if (underscoreCount == 1) { if(pReleaseName.endsWith("0"))
         * return "major-release"; else return "minor-release"; } else return "patch";
         */

    }

    public static int countIntegers(String input) {
        int count = 0;
        boolean isPreviousDigit = false;

        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                if (!isPreviousDigit) {
                    isPreviousDigit = true;
                }
                count++;
            } else {
                count = 0;
                isPreviousDigit = false;
            }
        }
        return count;
    }

}
