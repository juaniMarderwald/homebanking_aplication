package com.mindHub.homebanking.extra;

import com.mindHub.homebanking.models.Card;
import com.mindHub.homebanking.models.CardType;

import java.util.Set;

public class Utilities {

    public Utilities(){}

    //Obtengo un numero aleatorio entre 2 valores de minimo y maximo
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //Obtengo un Numero de cuenta aleatorio que comienza con VIN y tiene 8 digitos numericos
    public String getRandomAccountNumber(){
        return "VIN" + (int) ((Math.random() * (99999999 - 00000010)) + 00000010);
    }

    //Obtengo el número de la tarjeta de 16 dígitos aleatoriamente
    public String getRandomCardNumber(){
        return getRandomNumber(4000, 4999) +" "+getRandomNumber(1000,9999) +" "+getRandomNumber(1000,9999) +" "+getRandomNumber(1000,9999) ;
    }

    //Obtengo aleatoriamente el codigo de seguridad de 3 digitos para la tarjeta
    public Integer getRandomCVV(){
        Integer integer = (int)((Math.random() * (999 - 100)) + 100);
        return integer;
    }

}
