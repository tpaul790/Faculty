package domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumarComplex {
    private float re, im;
    static final String regex1 = "^([+-]?[1-9]*[0-9]+)([+-][1-9]+[0-9]*)[*]?i$"; //a+bi
    static final String regex2 = "^([+-]?[1-9]*[0-9]+)([+-]i)$";                 //a+i
    static final String regex3 = "^([+-]?[1-9]*[0-9]+)?[*]?i$";                  //bi
    static final String regex4 = "^([+-]?[1-9]*[0-9]+)$";                        //a

    public NumarComplex(String number) {
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        Pattern pattern3 = Pattern.compile(regex3);
        Pattern pattern4 = Pattern.compile(regex4);

        Matcher matcher1 = pattern1.matcher(number);
        Matcher matcher2 = pattern2.matcher(number);
        Matcher matcher3 = pattern3.matcher(number);
        Matcher matcher4 = pattern4.matcher(number);

        if(matcher1.matches()) {
            this.re = Float.parseFloat(matcher1.group(1));
            this.im = Float.parseFloat(matcher1.group(2));
        }else if(matcher2.matches()) {
            this.re = Float.parseFloat(matcher2.group(1));
            this.im = 1;
        }else if(matcher3.matches()) {
            this.re =0;
            if(matcher3.group(1) != null){
                this.im = Float.parseFloat(matcher3.group(1));
            }else
                this.im = 1;
        }else if(matcher4.matches()) {
            this.re = Float.parseFloat(matcher4.group(1));
            this.im = 0;
        }
        else{
            throw new IllegalArgumentException("String-ul nu reprezinta un numar complex valid!");
        }
    }

    public NumarComplex adunare(NumarComplex ot) {
        this.re += ot.re;
        this.im += ot.im;
        return this;
    }

    public NumarComplex scadere(NumarComplex ot) {
        this.re -= ot.re;
        this.im -= ot.im;
        return this;
    }

    public NumarComplex inmultire(NumarComplex ot) {
        float real = this.re * ot.re - this.im * ot.im;
        float imaginary = this.im*ot.re + this.re * ot.im;
        this.re = real;
        this.im = imaginary;
        return this;
    }

    public NumarComplex impartire(NumarComplex ot) {
        float numitor = ot.re * ot.re + ot.im * ot.im;
        if(numitor == 0)
            throw new ArithmeticException("Nu se poate imparti la 0");
        float real = (this.re * ot.re + this.im * ot.im)/numitor;
        float imaginary = (this.im * ot.re - this.re * ot.im)/numitor;
        this.re = real;
        this.im = imaginary;
        return this;
    }

    public NumarComplex conjugat(NumarComplex ot) {
        this.im = -this.im;
        return this;
    }

    public String toString() {
        if(this.im<0)
            return (this.re + "" + this.im + "*i");
        else{
            return (this.re + "+" + this.im + "*i");
        }
    }
}
