package test.prueba.appmapa.app.Model;

import java.io.Serializable;

/**
 * Created by gbravo on 7/29/14.
 */
public class DataSpinnerModel implements Serializable {
    public DataSpinnerModel(int index, String spinnerText, Number value ) {

        this.spinnerText = spinnerText;
        this.value = value;
        this.index = index;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public  Number getValue() {
        return value;
    }

    public int getIndex() {return index; }


    public String toString() {
        return spinnerText;
    }

    String spinnerText;
    Number value;
    int index;
}
