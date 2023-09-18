package utils.other;

public class NumMatrix {

    private int[][] prefixMatrixSum;

    public NumMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        prefixMatrixSum = new int[rows + 1][columns + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                prefixMatrixSum[i + 1][j + 1] = prefixMatrixSum[i + 1][j]
                        + prefixMatrixSum[i][j + 1]
                        - prefixMatrixSum[i][j]
                        + matrix[i][j];
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        return prefixMatrixSum[row2 + 1][col2 + 1]
                - prefixMatrixSum[row2 + 1][col1]
                - prefixMatrixSum[row1][col2 + 1]
                + prefixMatrixSum[row1][col1];
    }
}
