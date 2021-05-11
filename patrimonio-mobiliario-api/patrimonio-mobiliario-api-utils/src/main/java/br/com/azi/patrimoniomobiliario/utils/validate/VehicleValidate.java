package br.com.azi.patrimoniomobiliario.utils.validate;

public class VehicleValidate {

    public static boolean validarPlaca(String placa) {
        placa = placa.toUpperCase();
        return placa.matches("[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}|[A-Z]{3}-?[0-9]{4}");
    }

    public static boolean validarRenavam(String renavam) {
        renavam = ajustarRenavam(renavam);
        if(!renavam.matches("[0-9]{11}")) {
            return false;
        }

        int digitoVerificador = obterDigitoVerificadorRenavam(renavam);
        renavam = obterStringReversa(renavam);

        int resultado = calcularDigitoVerificadorRenavam(renavam);

        return resultado == digitoVerificador;
    }

    public static boolean validarChassi(String chassi) {
        chassi = chassi.replaceAll("[\\s\\-]", "");
        if(chassi.length() < 17 || chassi.matches("[a-z A-Z]{17}") || chassi.matches("[0-9]{17}")) {
            return false;
        }
        return true;
    }

    private static String ajustarRenavam(String renavam) {
        if(renavam.length() == 9) {
            return "00"+renavam;
        }
        return renavam;
    }

    private static int obterDigitoVerificadorRenavam(String renavam) {
        return Integer.parseInt(renavam.substring(10, 11));
    }

    private static String obterStringReversa(String renavam) {
        return new StringBuffer(renavam.substring(0, 10)).reverse().toString();
    }

    private static int calcularDigitoVerificadorRenavam(String renavam) {
        int soma = 0;

        for(int index = 0; index < 8; index++) {
            soma += Integer.parseInt(renavam.substring(index, index+1)) * (index+2);
        }
        soma += (Integer.parseInt(renavam.substring(8, 9))*2) + (Integer.parseInt(renavam.substring(9, 10))*3);
        int resultado = 11-(soma%11);
        if(resultado >= 10) {
            return 0;
        }
        return resultado;
    }
}
