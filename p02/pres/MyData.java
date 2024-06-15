package p02.pres;

import javax.swing.table.AbstractTableModel;

public
class MyData extends AbstractTableModel {


    private int[] data = new int[7];

    //public MyData(int[] data) {}

    public void setData(int[] data) {
        //System.out.println("setdata");
        this.data = data;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex >= 0)
            return Integer.class;
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[6-rowIndex];
    }

    @Override
    public int getRowCount() {
        return 7;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

}


