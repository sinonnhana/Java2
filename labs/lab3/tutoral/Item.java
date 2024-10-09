package tutoral;

public class Item implements Comparable<Item> {
    private String description;
    private int partNumber;

    public Item(String desc, int pnumber) {
        description = desc;
        partNumber = pnumber;
    }

    @Override
    public int compareTo(Item other) {
        // 先按部件号排序，如果部件号相同，则按描述排序
        int diff = Integer.compare(this.partNumber, other.partNumber);
        if (diff != 0) {
            return diff;
        }
        return this.description.compareTo(other.description);
    }

    @Override
    public String toString() {
        return "[no=" + partNumber + ", desc=" + description + "]";
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String getDescription() {
        return description;
    }
}