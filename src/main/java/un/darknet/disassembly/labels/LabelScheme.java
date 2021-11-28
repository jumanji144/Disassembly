package un.darknet.disassembly.labels;

public enum LabelScheme {

    ADDRESS,
    FRIENDLY;

    public static LabelScheme globalScheme = ADDRESS;

    public static void setGlobalScheme(LabelScheme scheme) {
        globalScheme = scheme;
    }

    public static LabelScheme getGlobalScheme() {
        return globalScheme;
    }

    public static final String[] fruits = new String[] {

            "Apple", "Watermelon", "Strawberry", "Pineapple", "Blueberry", "Grape", "Blackberry", "Cherry", "Lemon",
            "Orange", "Pumpkin", "Banana", "Peach", "Pear", "Pomegranate", "DragonFruit", "GrapeFruit", "Lime",
            "Mulberry", "Raspberry", "Cantaloupe", "Plum"

    };

    public static final String[] foodTypes = new String[] {

            "Pie", "Cookie", "Cake", "IceCream", "Shake", "Juice", "Jam", "Jelly", "Sandwich", "Jellybean", "Frosting",
            "Popsicle", "Muffin", "Tree", "Seed", "Pudding", "Marmalade", "Jello", "Macaroon", "Butter", "Cheesecake",
            "Cupcake", "Poptart", "Candy", "CoughDrop", "Lollipop", "Pastry", "Cupcake", "Meringue,"

    };

    public static String getRandomFruit() {
        return fruits[(int) (Math.random() * fruits.length)];
    }

    public static String getRandomFoodType() {
        return foodTypes[(int) (Math.random() * foodTypes.length)];
    }

    public static String getRandomFood() {
        return  getRandomFruit() + getRandomFoodType();
    }


    public static String generate(long address) {

        switch (globalScheme) {

            case ADDRESS:
                return String.format("%08X", address);
            case FRIENDLY:
                return getRandomFood().toLowerCase();
            default:
                return "";
        }

    }

}
