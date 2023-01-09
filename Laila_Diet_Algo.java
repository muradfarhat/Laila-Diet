import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/*

    Student : Murad Farhat 11820344

*/

/*   Implement calss for diet variables and methods  */
class Diet{
    
    public int[][] meals;
    private int[][] dynamicTable;
    private int[] dynamicPath;
        
    Diet(int N){
        this.meals = new int[3][N];
        this.dynamicTable = new int[3][N];
        this.dynamicPath = new int[N];
    }
    
    /********************* Start Divide And Conquer Function *************************/
    
    public int[] calcCalories(int day, int mealType){
        int[] values = new int[2];
        int[] recursiveMealValue = new int[2];
        int[][] finalValues = new int[3][2];
        if(day != 1){
            for(int i = 0; i < 3; i++){
                recursiveMealValue = calcCalories(day - 1, mealType == -1 ? i + 1 : mealType);
                
                int index1;
                int index2;
                
                if(recursiveMealValue[1] == 1){
                    index1 = 1;
                    index2 = 2;
                }else if(recursiveMealValue[1] == 2){
                    index1 = 0;
                    index2 = 2;
                }else{
                    index1 = 0;
                    index2 = 1;
                }
                
                int firstTempValue = this.meals[index1][day - 1] + recursiveMealValue[0];
                int secontTempValue = this.meals[index2][day - 1] + recursiveMealValue[0];
                
                values[0] = Math.min(firstTempValue, secontTempValue);
                if(values[0] == firstTempValue)
                    values[1] = index1 + 1;
                else
                    values[1] = index2 + 1;
                
                if(mealType != -1)
                    return values;
                else{
                    finalValues[i][0] = values[0];
                    finalValues[i][1] = values[1];
                }
            }
            
            
            values[0] = Math.min(finalValues[0][0], Math.min(finalValues[1][0], finalValues[2][0]));
            return values;
            
        }
        
        values[0] = this.meals[mealType - 1][day - 1];
        values[1] = mealType;
        return values;   
    }
    
    /********************* End Divide And Conquer Function *************************/
    
    /********************* Start Dynamic Programming Function *************************/
    
    public int dynamicDietFillTable(int day){
        int pathValue;
        int type = 0;
        int[] values = new int[3];
        
        for(int i = 0; i < 3; i++){
            pathValue = 0;
            for(int j = 0; j < day; j++){
                if(j == 0){
                    pathValue += this.meals[i][j];
                    type = i;
                }else{
                    if(type == 0){
                        if(this.meals[1][j] < this.meals[2][j]){
                            pathValue += this.meals[1][j];
                            type = 1;
                        }else{
                            pathValue += this.meals[2][j];
                            type = 2;
                        }
                    }
                    else if(type == 1){
                        if(this.meals[0][j] < this.meals[2][j]){
                            pathValue += this.meals[0][j];
                            type = 0;
                        }else{
                            pathValue += this.meals[2][j];
                            type = 2;
                        }
                    }
                    else{
                        if(this.meals[0][j] < this.meals[1][j]){
                            pathValue += this.meals[0][j];
                            type = 0;
                        }else{
                            pathValue += this.meals[1][j];
                            type = 1;
                        }
                    }
                }
            }
            values[i] = pathValue;
        }
        
        if(values[0] < values[1]){
            if(values[0] < values[2])
                type = 0;
            else
                type = 2;
        }
        else {
            if(values[1] < values[2])
                type = 1;
            else 
                type = 2;
        }
        
        /***************************** Fill Tabels  *********************/
        pathValue = 0;
        for(int i = 0; i < day; i++){
            if(i == 0){
                pathValue += this.meals[type][i];
                this.dynamicTable[0][i] = this.meals[0][i];
                this.dynamicTable[1][i] = this.meals[1][i];
                this.dynamicTable[2][i] = this.meals[2][i];
                this.dynamicPath[i] = type;
            }else{
                if(type == 0){
                    if(this.meals[1][i] < this.meals[2][i]){
                        pathValue += this.meals[1][i];
                        this.dynamicTable[1][i] = pathValue;
                        this.dynamicTable[2][i] = this.meals[2][i];
                        this.dynamicTable[0][i] = this.meals[0][i];
                        type = 1;
                        this.dynamicPath[i] = type;
                    }else{
                        pathValue += this.meals[2][i];
                        this.dynamicTable[2][i] = pathValue;
                        this.dynamicTable[1][i] = this.meals[1][i];
                        this.dynamicTable[0][i] = this.meals[0][i];
                        type = 2;
                        this.dynamicPath[i] = type;
                    }
                }
                else if(type == 1){
                    if(this.meals[0][i] < this.meals[2][i]){
                        pathValue += this.meals[0][i];
                        this.dynamicTable[0][i] = pathValue;
                        this.dynamicTable[2][i] = this.meals[2][i];
                        this.dynamicTable[1][i] = this.meals[1][i];
                        type = 0;
                        this.dynamicPath[i] = type;
                    }else{
                        pathValue += this.meals[2][i];
                        this.dynamicTable[2][i] = pathValue;
                        this.dynamicTable[1][i] = this.meals[1][i];
                        this.dynamicTable[0][i] = this.meals[0][i];
                        type = 2;
                        this.dynamicPath[i] = type;
                    }
                }
                else{
                    if(this.meals[0][i] < this.meals[1][i]){
                        pathValue += this.meals[0][i];
                        this.dynamicTable[0][i] = pathValue;
                        this.dynamicTable[1][i] = this.meals[1][i];
                        this.dynamicTable[2][i] = this.meals[2][i];
                        type = 0;
                        this.dynamicPath[i] = type;
                    }else{
                        pathValue += this.meals[1][i];
                        this.dynamicTable[1][i] = pathValue;
                        this.dynamicTable[0][i] = this.meals[0][i];
                        this.dynamicTable[2][i] = this.meals[2][i];
                        type = 1;
                        this.dynamicPath[i] = type;
                    }
                }
            }
        }
        
        /***************************** Fill Tabels  *********************/
        
        return Math.min(values[0], Math.min(values[1], values[2]));
        
    }
    
    /********************* End Dynamic Programming Function *************************/
    
    /********************* Start print the sequence of moves Function *************************/
    public void printPath(){
        for(int i = 0; i < this.dynamicPath.length; i++){
            System.out.println(this.dynamicPath[i]);
        }
    }
    /********************* End print the sequence of moves Function *************************/
}

public class Solution {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Enter number of days
        int N;
        N = in.nextInt();
        
        Diet diet = new Diet(N);
        
        String calories;
        
        for(int i = 0; i < 3; i++){
            switch(i){
                case 0:
                    for(int j = 0; j < N; j++){
                        calories = in.next();
                        diet.meals[i][j] = Integer.parseInt(calories);
                    }
                    break;
                    
                case 1:
                    for(int j = 0; j < N; j++){
                        calories = in.next();
                        diet.meals[i][j] = Integer.parseInt(calories);
                    }
                    break;
                    
                case 2:
                    for(int j = 0; j < N; j++){
                        calories = in.next();
                        diet.meals[i][j] = Integer.parseInt(calories);
                    }
                    break;
            }
        }
        
        
        
//         int[] minCalorie = new int[2];
//         minCalorie = diet.calcCalories(N, -1);
        
//         System.out.println(minCalorie[0]);
        
        System.out.println(diet.dynamicDietFillTable(N));
    }
}