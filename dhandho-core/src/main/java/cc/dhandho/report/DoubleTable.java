package cc.dhandho.report;

public class DoubleTable {

	protected Double[][] table;

	protected int rows;

	public DoubleTable() {
		this.table = new Double[1000][];
	}

	public Double get(int row, int col) {
		return table[row][col];
	}

	public Double[] cloneRow(int row) {
		Double[] row1 = this.getRow(row);
		Double[] rt = new Double[row1.length];
		System.arraycopy(row1, 0, rt, 0, rt.length);
		return rt;
	}

	public Double[] getRow(int row) {
		return table[row];
	}

	public int addRow(Double[] row) {

		if (rows + 1 == table.length) {
			Double[][] table2 = new Double[table.length + 1000][];
			System.arraycopy(table, 0, table2, 0, rows);
			table = table2;
		}
		table[rows] = row;
		return rows++;

	}

	public void dividRowBy(int row, double d) {
		Double[] valueArray = this.table[row];
		for (int i = 0; i < valueArray.length; i++) {
			Double d1 = valueArray[i];

			if (d1 != null) {

				if (d1 == 0) {
					valueArray[i] = null;
				} else {
					valueArray[i] = d / d1;
				}
			}
		}
	}

	public void multipleRow(int row, double d) {
		Double[] valueArray = this.table[row];
		for (int i = 0; i < valueArray.length; i++) {
			Double d1 = valueArray[i];

			if (d1 != null) {
				valueArray[i] = d * d1;
			}
		}
	}

	public DoubleTable dividBy(double d) {
		for (int i = 0; i < this.rows; i++) {
			Double[] row = table[i];
			for (int j = 0; j < row.length; j++) {
				if (row[j] == null) {
					continue;
				}
				if (row[j].isNaN()) {
					row[j] = null;
				}
				row[j] = d / row[j];
			}
		}

		return this;
	}

	public DoubleTable multiple(double d) {
		for (int i = 0; i < this.rows; i++) {
			Double[] row = table[i];
			for (int j = 0; j < row.length; j++) {
				if (row[j] == null) {
					continue;
				}
				if (row[j].isNaN()) {
					row[j] = null;
				}
				row[i] = d * row[i];
			}
		}

		return this;
	}

}
