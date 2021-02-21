package rarityeg.commons;

public class CommandValidator {
    public enum ArgType {
        STRING,
        NUMBER,
        BOOLEAN
    }

    /**
     * Validate arguments using type assertion.
     *
     * @param args  Arguments of this command.
     * @param types ArgType to compare.
     * @return If the arguments matches those types.
     * @see ArgType
     */
    public static boolean validate(String[] args, ArgType... types) {
        if (args.length < types.length) {
            return false;
        }
        for (int i = 0; i <= types.length; i++) {
            if (types[i] == ArgType.STRING) {
                continue;
            }
            if (types[i] == ArgType.NUMBER) {
                if (!isNumber(args[i])) {
                    return false;

                }
                continue;
            }
            if (types[i] == ArgType.BOOLEAN) {
                if (!isBoolean(args[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the string is a boolean, like "y...", "n...", "true", "false", "t", "f"
     *
     * @param s The string to check.
     * @return If the string is a boolean.
     */
    protected static boolean isBoolean(String s) {
        String sLow = s.toLowerCase();
        return sLow.startsWith("y") || sLow.startsWith("n") || sLow.equals("true") || sLow.equals("false") || sLow.equals("t") || sLow.equals("f");
    }

    /**
     * Check if the string contains a number.
     *
     * @param s The string to check.
     * @return If the string contains a number.
     */
    protected static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
    }

}
