package genesis;

import java.util.List;

public class Scripts {
    private List<String> createTable;
    private List<String> insert;
    public List<String> getCreateTable() {
        return createTable;
    }
    public void setCreateTable(List<String> createTable) {
        this.createTable = createTable;
    }
    public List<String> getInsert() {
        return insert;
    }
    public void setInsert(List<String> insert) {
        this.insert = insert;
    }
    
}
