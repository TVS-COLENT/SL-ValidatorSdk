package com.totvs.sl.validator.sdk.constraints.br.cnpj;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator para CNPJ no novo padrão alfanumérico.
 *
 * <p>
 * O novo formato permite caracteres alfanuméricos (A-Z, 0-9) nas 12 primeiras
 * posições (base + filial) e mantém 2 dígitos verificadores numéricos.
 *
 * <p>
 * Regras:
 * <ul>
 * <li>Comprimento total: 14 caracteres (sem formatação) ou 18 (com formatação
 * XX.XXX.XXX/XXXX-XX)</li>
 * <li>Caracteres permitidos: A-Z (maiúsculos) e 0-9 nas posições de
 * base/filial</li>
 * <li>Os 2 últimos dígitos são verificadores numéricos calculados com base nos
 * valores ASCII</li>
 * <li>O cálculo dos dígitos verificadores utiliza os pesos padrão com valores
 * baseados na tabela ASCII</li>
 * </ul>
 */
public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

	private static final int TAMANHO_CNPJ = 14;
	private static final int ASCII_ZERO = 48;
	private static final int[] PESOS_PRIMEIRO_DIGITO = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] PESOS_SEGUNDO_DIGITO = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	/**
	 * Valida um CNPJ de forma isolada, sem depender do contexto de Bean Validation.
	 *
	 * @param value CNPJ a ser validado (com ou sem formatação)
	 * @return {@code true} se o CNPJ for válido, {@code false} caso contrário.
	 *         Retorna {@code false} para valores nulos ou em branco.
	 */
	public static boolean isValid(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}

		String cnpj = sanitize(value);

		if (cnpj.length() != TAMANHO_CNPJ) {
			return false;
		}

		if (!possuiCaracteresValidos(cnpj)) {
			return false;
		}

		if (possuiTodosCaracteresIguais(cnpj)) {
			return false;
		}

		return saoDigitosVerificadoresValidos(cnpj);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		return isValid(value);
	}

	private static String sanitize(String cnpj) {
		return cnpj.replaceAll("[./-]", "").toUpperCase();
	}

	private static boolean possuiCaracteresValidos(String cnpj) {
		for (int i = 0; i < cnpj.length() - 2; i++) {
			char c = cnpj.charAt(i);
			if (!Character.isLetterOrDigit(c) || Character.isLowerCase(c)) {
				return false;
			}
		}
		return Character.isDigit(cnpj.charAt(12)) && Character.isDigit(cnpj.charAt(13));
	}

	private static boolean possuiTodosCaracteresIguais(String cnpj) {
		char primeiro = cnpj.charAt(0);
		for (int i = 1; i < cnpj.length(); i++) {
			if (cnpj.charAt(i) != primeiro) {
				return false;
			}
		}
		return true;
	}

	private static boolean saoDigitosVerificadoresValidos(String cnpj) {
		int primeiroDigito = calcularDigitoVerificador(cnpj, PESOS_PRIMEIRO_DIGITO);
		int segundoDigito = calcularDigitoVerificador(cnpj, PESOS_SEGUNDO_DIGITO);

		return primeiroDigito == Character.getNumericValue(cnpj.charAt(12))
		        && segundoDigito == Character.getNumericValue(cnpj.charAt(13));
	}

	private static int calcularDigitoVerificador(String cnpj, int[] pesos) {
		int soma = 0;
		for (int i = 0; i < pesos.length; i++) {
			int valorCaractere = converterParaValorAscii(cnpj.charAt(i));
			soma += valorCaractere * pesos[i];
		}

		int resto = soma % 11;
		return resto < 2 ? 0 : 11 - resto;
	}

	/**
	 * Converte o caractere para valor numérico baseado na tabela ASCII.
	 * <p>
	 * Para dígitos (0-9): retorna o valor numérico (0-9). Para letras (A-Z):
	 * retorna o valor ASCII menos 48 (A=17, B=18, ..., Z=42).
	 */
	private static int converterParaValorAscii(char c) {
		return c - ASCII_ZERO;
	}
}
