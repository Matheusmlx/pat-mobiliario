package br.com.azi.patrimoniomobiliario.utils.validate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class VehicleValidateTest {

    @Test
    public void deveAfirmarVerdadeiroParaPlacasValidas() {
        assertTrue(VehicleValidate.validarPlaca("LMA-9744"));
        assertTrue(VehicleValidate.validarPlaca("JTE-3780"));
        assertTrue(VehicleValidate.validarPlaca("HZN7948"));
        assertTrue(VehicleValidate.validarPlaca("EMG-5950"));
        assertTrue(VehicleValidate.validarPlaca("EYG-5814"));
        assertTrue(VehicleValidate.validarPlaca("DXV-0151"));
        assertTrue(VehicleValidate.validarPlaca("RPW-3396"));
        assertTrue(VehicleValidate.validarPlaca("RFG-3990"));
        assertTrue(VehicleValidate.validarPlaca("HIG-2246"));
        assertTrue(VehicleValidate.validarPlaca("RXH-1167"));
        assertTrue(VehicleValidate.validarPlaca("RST-1939"));
        assertTrue(VehicleValidate.validarPlaca("nxw-9697"));
        assertTrue(VehicleValidate.validarPlaca("LYT-5109"));
        assertTrue(VehicleValidate.validarPlaca("hre8311"));
        assertTrue(VehicleValidate.validarPlaca("ODR-7943"));
    }

    @Test
    public void deveAfirmarFalsoParaPlacasInvalidas() {
        assertFalse(VehicleValidate.validarPlaca("PN2-7634"));
        assertFalse(VehicleValidate.validarPlaca("BPM-A090"));
        assertFalse(VehicleValidate.validarPlaca("ENB31093"));
        assertFalse(VehicleValidate.validarPlaca("ans2-1676"));
        assertFalse(VehicleValidate.validarPlaca("FAG-44965"));
        assertFalse(VehicleValidate.validarPlaca("HPAr8661"));
        assertFalse(VehicleValidate.validarPlaca("MKS-9E24"));
        assertFalse(VehicleValidate.validarPlaca("IK1-3857"));
        assertFalse(VehicleValidate.validarPlaca("QD3007E"));
        assertFalse(VehicleValidate.validarPlaca("MAK-963S"));
        assertFalse(VehicleValidate.validarPlaca("EOV-BB46"));
    }

    @Test
    public void deveAfirmarVerdadeiroParaRenavamsValidos() {
        assertTrue(VehicleValidate.validarRenavam("52844824529"));
        assertTrue(VehicleValidate.validarRenavam("52724698208"));
        assertTrue(VehicleValidate.validarRenavam("63130680271"));
        assertTrue(VehicleValidate.validarRenavam("92535227312"));
        assertTrue(VehicleValidate.validarRenavam("59497608405"));
        assertTrue(VehicleValidate.validarRenavam("49937110607"));
        assertTrue(VehicleValidate.validarRenavam("66746783577"));
        assertTrue(VehicleValidate.validarRenavam("19213665779"));
        assertTrue(VehicleValidate.validarRenavam("28284644994"));
        assertTrue(VehicleValidate.validarRenavam("37170799947"));
        assertTrue(VehicleValidate.validarRenavam("33090911918"));
        assertTrue(VehicleValidate.validarRenavam("77560559419"));
        assertTrue(VehicleValidate.validarRenavam("26125939560"));
        assertTrue(VehicleValidate.validarRenavam("46738720842"));
    }

    @Test
    public void deveAfirmarFalsoParaRenavamsInvalidos() {
        assertFalse(VehicleValidate.validarRenavam("12345678910"));
        assertFalse(VehicleValidate.validarRenavam("46338720842"));
        assertFalse(VehicleValidate.validarRenavam("77460359419"));
        assertFalse(VehicleValidate.validarRenavam("33190911918"));
        assertFalse(VehicleValidate.validarRenavam("9876"));
    }

    @Test
    public void deveAfirmarVerdadeiroParaChassisValidos() {
        assertTrue(VehicleValidate.validarChassi("5FN-YF3H5-5EB026654"));
        assertTrue(VehicleValidate.validarChassi("1MEFM59S3YA622693"));
        assertTrue(VehicleValidate.validarChassi("2T1KU40E09C134221"));
        assertTrue(VehicleValidate.validarChassi("1GCEC19R0WR112404"));
        assertTrue(VehicleValidate.validarChassi("JM1BL1W74C1532800"));
        assertTrue(VehicleValidate.validarChassi("5TFPX4EN5DX017797"));
        assertTrue(VehicleValidate.validarChassi("5J6TF1H51AL015702"));
        assertTrue(VehicleValidate.validarChassi("1GBJK39DX6E165432"));
        assertTrue(VehicleValidate.validarChassi("JH4KA7532NC036794"));
        assertTrue(VehicleValidate.validarChassi("JH4DB7660SS001798"));
        assertTrue(VehicleValidate.validarChassi("WBSPM9C52BE202514"));
        assertTrue(VehicleValidate.validarChassi("JH4DA1850HS006058"));
        assertTrue(VehicleValidate.validarChassi("JH4CC2660PC002236"));
        assertTrue(VehicleValidate.validarChassi("JH4DB1550PS001359"));
    }

    @Test
    public void deveAfirmarFalsoParaChassisInvalidos() {
        assertFalse(VehicleValidate.validarChassi("5N1AN0NU1BC506916"));
        assertFalse(VehicleValidate.validarChassi("1GKEV16KXLF538649"));
        assertFalse(VehicleValidate.validarChassi("1JCCN18N2CT047552"));
        assertFalse(VehicleValidate.validarChassi("1FUJA6CV54DM34063"));
        assertFalse(VehicleValidate.validarChassi("1GTEC14W3YZ246726"));
        assertFalse(VehicleValidate.validarChassi("JH4KA4661JC005061"));
        assertFalse(VehicleValidate.validarChassi("JH4DA934XNS001774"));
        assertFalse(VehicleValidate.validarChassi("JH4DA3448HS012198"));
        assertFalse(VehicleValidate.validarChassi("ZAMBC38A050014565"));
        assertFalse(VehicleValidate.validarChassi("WD5WD641425381291"));
        assertFalse(VehicleValidate.validarChassi("JH4DB1659NS000627"));
        assertFalse(VehicleValidate.validarChassi("4JGBF71E28A429191"));
    }
}
