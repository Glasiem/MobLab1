package com.example.moblab1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Spinner operatorSpinner;
    private Spinner sizeSpinner;
    private EditText[][] matrix1 = new EditText[4][4];
    private EditText[][] matrix2 = new EditText[4][4];
    private TextView resultTextView;
    private String selectedSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
        initializeViews();
        setListeners();
        restoreSavedValues();
    }

    private void initializeViews() {
        initializeMatrix();
        operatorSpinner = findViewById(R.id.spinnerOperator);
        sizeSpinner = findViewById(R.id.spinnerSize);
        resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setTextSize(14);
        resultTextView.setTextColor(getResources().getColor(R.color.gray));
        resultTextView = findViewById(R.id.resultTextView);
    }



    private void initializeMatrix() {
        matrix1[0][0] = findViewById(R.id.editText11);
        matrix1[0][1] = findViewById(R.id.editText12);
        matrix1[0][2] = findViewById(R.id.editText13);
        matrix1[0][3] = findViewById(R.id.editText14);
        matrix1[1][0] = findViewById(R.id.editText21);
        matrix1[1][1] = findViewById(R.id.editText22);
        matrix1[1][2] = findViewById(R.id.editText23);
        matrix1[1][3] = findViewById(R.id.editText24);
        matrix1[2][0] = findViewById(R.id.editText31);
        matrix1[2][1] = findViewById(R.id.editText32);
        matrix1[2][2] = findViewById(R.id.editText33);
        matrix1[2][3] = findViewById(R.id.editText34);
        matrix1[3][0] = findViewById(R.id.editText41);
        matrix1[3][1] = findViewById(R.id.editText42);
        matrix1[3][2] = findViewById(R.id.editText43);
        matrix1[3][3] = findViewById(R.id.editText44);
        matrix2[0][0] = findViewById(R.id.editText11matrix2);
        matrix2[0][1] = findViewById(R.id.editText12matrix2);
        matrix2[0][2] = findViewById(R.id.editText13matrix2);
        matrix2[0][3] = findViewById(R.id.editText14matrix2);
        matrix2[1][0] = findViewById(R.id.editText21matrix2);
        matrix2[1][1] = findViewById(R.id.editText22matrix2);
        matrix2[1][2] = findViewById(R.id.editText23matrix2);
        matrix2[1][3] = findViewById(R.id.editText24matrix2);
        matrix2[2][0] = findViewById(R.id.editText31matrix2);
        matrix2[2][1] = findViewById(R.id.editText32matrix2);
        matrix2[2][2] = findViewById(R.id.editText33matrix2);
        matrix2[2][3] = findViewById(R.id.editText34matrix2);
        matrix2[3][0] = findViewById(R.id.editText41matrix2);
        matrix2[3][1] = findViewById(R.id.editText42matrix2);
        matrix2[3][2] = findViewById(R.id.editText43matrix2);
        matrix2[3][3] = findViewById(R.id.editText44matrix2);
    }

    private void setListeners() {
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = sizeSpinner.getSelectedItem().toString();
                saveValue("size", selectedSize);
                updateMatrixSize();
                saveMatrix();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                saveMatrix();
            }
        });
        operatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveValue("operator", operatorSpinner.getSelectedItem().toString());
                saveMatrix();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button determinantMatrix1Button = findViewById(R.id.determinantButton1);
        determinantMatrix1Button.setOnClickListener(v -> findDeterminantInterface(matrix1));
        Button determinantMatrix2Button = findViewById(R.id.determinantButton2);
        determinantMatrix2Button.setOnClickListener(v -> findDeterminantInterface(matrix2));


        Button reverseMatrix1Button = findViewById(R.id.reverseButton1);
        reverseMatrix1Button.setOnClickListener(v -> findInverseInterface(matrix1));
        Button reverseMatrix2Button = findViewById(R.id.reverseButton2);
        reverseMatrix2Button.setOnClickListener(v -> findInverseInterface(matrix2));

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(v -> calculateResult());

        Button diagramsButton = findViewById(R.id.diagramsButton);
        diagramsButton.setOnClickListener(v -> {
            saveMatrix();
            Intent intent = new Intent(MainActivity.this, DiagramsActivity.class);
            int size = getSize();
            intent.putExtra("size", size);
            for (int i = 0; i < size; i++) {
                double s1 = 0;
                double s2 = 0;
                for (int j = 0; j < size; j++) {
                    s1 += getNum(matrix1,i,j);
                    s2 += getNum(matrix2,i,j);
                }
                intent.putExtra("matrix1row" + i,s1);
                intent.putExtra("matrix2row" + i,s2);
            }
            startActivity(intent);
        });

        Button navigateButton = findViewById(R.id.navigateButton);
        navigateButton.setOnClickListener(v -> {
            saveMatrix();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });
    }

    public void findInverseInterface(EditText[][] matrix){
        int n = getSize();
        try {
            String res = findInverse(matrix, n);
            resultTextView.setText(String.valueOf(res));
            saveMatrix();
        }
        catch (Exception e){
            resultTextView.setText("wrong input");
        }
    }

    public String findInverse(EditText[][] matrix, int n) {
        double[][] augmentedMatrix = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = getNum(matrix,i,j);
                augmentedMatrix[i][j + n] = (i == j) ? 1 : 0;
            }
        }

        for (int i = 0; i < n; i++) {
            double divisor = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= divisor;
            }

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = augmentedMatrix[i][j + n];
            }
        }

        String res = matrixToString(n, inverseMatrix);

        return res;
    }

   private static String matrixToString(int n, double[][] matrix) {
       int[] maxWidths = new int[n];

       for (int j = 0; j < n; j++) {
           for (int i = 0; i < n; i++) {
               String formattedValue = String.format("%.2f", matrix[i][j]).replaceAll("\\.?0*$", "");
               int width = formattedValue.trim().length();
               if (width > maxWidths[j]) {
                   maxWidths[j] = width;
               }
           }
       }

       StringBuilder res = new StringBuilder();

       for (int i = 0; i < n; i++) {
           String result = "";
           for (int j = 0; j < n; j++) {
               String num = String.format("%.2f", matrix[i][j]).trim().replaceAll("\\.?0*$", "");
               for (int k = num.length(); k <= maxWidths[j]; k++) {
                   num += " ";
               }
               result += num;
           }
           result += "\n";
           res.append(result);
       }
       return res.toString();
   }



    @SuppressLint("SetTextI18n")
    private void findDeterminantInterface(EditText[][] matrix) {
        int n = getSize();
        try {
        double res = findDeterminant(matrix, n);
        resultTextView.setText(String.valueOf(res));
        saveMatrix();
        }
        catch (Exception e){
            resultTextView.setText("wrong input");
        }
    }

    private int getSize() {
        int n = ((ArrayAdapter<String>) sizeSpinner.getAdapter()).getPosition(sizeSpinner.getSelectedItem().toString())+1;
        return n;
    }

    private double findDeterminant(EditText[][] matrix, int n) {

        if (n == 1) {
            return getNum(matrix,0,0);
        }

        double determinant = 0;

        for (int i = 0; i < n; i++) {
            int sign = (i % 2 == 0) ? 1 : -1;

            EditText[][] minor = getMinor(matrix, 0, i);

            double minorDeterminant = findDeterminant(minor, n-1);
            double num = getNum(matrix, 0, i);
            determinant += sign * num * minorDeterminant;
        }

        return determinant;
    }

    private static double getNum(EditText[][] matrix, int i, int j) {
        double num = matrix[i][j].getText().toString().isEmpty()? 0 : Double.parseDouble(matrix[i][j].getText().toString());
        return num;
    }

    public static EditText[][] getMinor(EditText[][] matrix, int rowToRemove, int colToRemove) {
        int n = matrix.length;
        EditText[][] minor = new EditText[n - 1][n - 1];
        int minorRow = 0;
        int minorCol;

        for (int i = 0; i < n; i++) {
            if (i == rowToRemove) {
                continue;
            }

            minorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == colToRemove) {
                    continue;
                }
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }

            minorRow++;
        }

        return minor;
    }
    private void restoreSavedValues() {
        String savedOperator = getSavedValue("operator");
        if (savedOperator != null && !savedOperator.isEmpty()) {
            int operatorPosition = ((ArrayAdapter<String>) operatorSpinner.getAdapter()).getPosition(savedOperator);
            operatorSpinner.setSelection(operatorPosition);
        }

        String savedSize = getSavedValue("size");
        if (savedSize != null && !savedSize.isEmpty()) {
            int sizePosition = ((ArrayAdapter<String>) sizeSpinner.getAdapter()).getPosition(savedSize);
            sizeSpinner.setSelection(sizePosition);
            selectedSize = sizeSpinner.getSelectedItem().toString();
            updateMatrixSize();
        }
        else {
            sizeSpinner.setSelection(3);
        }
        matrixRestore();
    }

    private void matrixRestore() {
        int size = getSize()-1;
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                matrix1[i][j].setText(getSavedValue("matrix1editText" + i + j));
                matrix2[i][j].setText(getSavedValue("matrix2editText" + i + j));
            }
        }
    }

    private String getSavedValue(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void saveValue(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void saveMatrix() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int size = ((ArrayAdapter<String>) sizeSpinner.getAdapter()).getPosition(sizeSpinner.getSelectedItem().toString());
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                editor.putString("matrix1editText" + i + j, matrix1[i][j].getText().toString());
                editor.putString("matrix2editText" + i + j, matrix2[i][j].getText().toString());
            }
        }
        editor.apply();
    }

    private void calculateResult() {
        int n = getSize();
        String operator = operatorSpinner.getSelectedItem().toString();
        String res = "";
        try {
            double[][] result = new double[n][n];
            switch (operator) {
                case "+":
                    result = addMatrices();
                    break;
                case "-":
                    result = subtractMatrices();
                    break;
                case "*":
                    result = multiplyMatrices();
                    break;
            }
            saveMatrix();
            res = matrixToString(n,result);
        } catch (Exception e) {
            res = "wrong input";
            System.out.println(e.toString());
        }
        resultTextView.setText(res);
    }


    private void updateMatrixSize() {
        int size = getSize();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(i < size && j < size) {
                    matrix1[i][j].setVisibility(View.VISIBLE);
                    matrix2[i][j].setVisibility(View.VISIBLE);
                }
                else {
                    matrix1[i][j].setVisibility(View.INVISIBLE);
                    matrix1[i][j].setText("");
                    matrix2[i][j].setVisibility(View.INVISIBLE);
                    matrix1[i][j].setText("");
                }
            }
        }
    }

    // Method to add two matrices
    private double[][] addMatrices() {
        int size = getSize();
    
        // Initialize the result matrix
        double[][] resultMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double num1 = getNum(matrix1, i, j);
                double num2 = getNum(matrix2, i, j);
                resultMatrix[i][j] = num1 + num2;
            }
        }

        return resultMatrix;
    }

    private double[][] subtractMatrices() {
        int size = getSize();

        double[][] resultMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double num1 = getNum(matrix1, i, j);
                double num2 = getNum(matrix2, i, j);
                resultMatrix[i][j] = num1 - num2;
            }
        }

        return resultMatrix;
    }

    private double[][] multiplyMatrices() {
        int size = getSize();

        double[][] resultMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resultMatrix[i][j] = 0;
                for (int k = 0; k < size; k++) {
                    double num1 = getNum(matrix1, i, k);
                    double num2 = getNum(matrix2, k, j);
                    resultMatrix[i][j] += num1 * num2;
                }
            }
        }

        return resultMatrix;
    }

}
