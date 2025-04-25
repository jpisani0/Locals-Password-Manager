package Vault;

abstract class TEST_Vault {
    public static void main(String[] args) {
        Group group1 = new Group("group1", null);

        Entry entry1 = new Entry();
        Entry entry2 = new Entry();
        Entry entry3 = new Entry();

        entry1.setEntryName("entry1");
        entry2.setEntryName("entry2");
        entry3.setEntryName("entry3");

        entry1.setUsername("user1");
        entry2.setUsername("user2");
        entry3.setUsername("user3");

        entry1.setPassword("passwd1");
        entry2.setPassword("passwd2");
        entry3.setPassword("passwd3");

        entry1.setUrl("url1");
        entry2.setUrl("url2");
        entry3.setUrl("url3");

        entry1.setNotes("notes1");
        entry2.setNotes("notes2");
        entry3.setNotes("notes3");

        group1.addEntry(entry1);
        group1.addEntry(entry2);
        group1.addEntry(entry3);

        System.out.println(group1.getEntry(0).getEntryName());
        System.out.println(group1.getEntry(1).getEntryName());
        System.out.println(group1.getEntry(2).getEntryName());
        System.out.println();
        System.out.println(group1.getEntry(0).getUsername());
        System.out.println(group1.getEntry(1).getUsername());
        System.out.println(group1.getEntry(2).getUsername());
        System.out.println();
        System.out.println(group1.getEntry(0).getPassword());
        System.out.println(group1.getEntry(1).getPassword());
        System.out.println(group1.getEntry(2).getPassword());
        System.out.println();
        System.out.println(group1.getEntry(0).getUrl());
        System.out.println(group1.getEntry(1).getUrl());
        System.out.println(group1.getEntry(2).getUrl());
        System.out.println();
        System.out.println(group1.getEntry(0).getNotes());
        System.out.println(group1.getEntry(1).getNotes());
        System.out.println(group1.getEntry(2).getNotes());
        System.out.println();
        group1.listEntries();
        System.out.println();
        group1.moveEntry(2, 1);
        group1.listEntries();
        System.out.println();
        group1.removeEntry(1);
        group1.listEntries();
        System.out.println();
    }
}
