package br.com.murilofarias.sharedpay.util;

public class CpfUtils {

    public static String eliminateDotsAndDashes(String cpf){
        String withoutDots = cpf.replace(".", "");
        String withoutDotsAndDashes = withoutDots.replace("-", "");
        return withoutDotsAndDashes;
    }
}
