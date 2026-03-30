package com.totvs.sl.validator.sdk.constraints.br.cpf;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator para CPF (Cadastro de Pessoas Físicas).
 *
 * <p>
 * Regras:
 * <ul>
 * <li>Comprimento total: 11 dígitos numéricos (sem formatação) ou 14 caracteres
 * (com formatação XXX.XXX.XXX-XX)</li>
 * <li>Caracteres permitidos: somente dígitos (0-9)</li>
 * <li>Não permite todos os dígitos iguais (ex: 000.000.000-00)</li>
 * <li>Os 2 últimos dígitos são verificadores calculados pelo Módulo 11</li>
 * </ul>
 */
public class CPFValidator implements ConstraintValidator<CPF, String> {

	private static final int TAMANHO_CPF = 11;
	private static final int[] PESOS_PRIMEIRO_DIGITO = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] PESOS_SEGUNDO_DIGITO = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	/**
	 * Valida um CPF de forma isolada, sem depender do contexto de Bean Validation.
	 *
	 * @param value CPF a ser validado (com ou sem formatação)
	 * @return {@code true} se o CPF for válido, {@code false} caso contrário.
	 *         Retorna {@code false} para valores nulos ou em branco.
	 */
	public static boolean isValid(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}

		String cpf = sanitize(value);

		if (cpf.length() != TAMANHO_CPF) {
			return false;
		}

		if (!possuiApenasDigitos(cpf)) {
			return false;
		}

		if (possuiTodosDigitosIguais(cpf)) {
			return false;
		}

		return saoDigitosVerificadoresValidos(cpf);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		return isValid(value);
	}

	/**
	 * Remove formatação (pontos, hífens)
	 *
	 * @param cpf CPF com ou sem formatação
	 * @return CPF sanitizado contendo apenas caracteres numéricos
	 */
	public static String sanitize(String cpf) {
		return Objects.nonNull(cpf) ? cpf.replaceAll("[.-]", "") : null;
	}

	private static boolean possuiApenasDigitos(String cpf) {
		for (int i = 0; i < cpf.length(); i++) {
			if (!Character.isDigit(cpf.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean possuiTodosDigitosIguais(String cpf) {
		char primeiro = cpf.charAt(0);
		for (int i = 1; i < cpf.length(); i++) {
			if (cpf.charAt(i) != primeiro) {
				return false;
			}
		}
		return true;
	}

	private static boolean saoDigitosVerificadoresValidos(String cpf) {
		int primeiroDigito = calcularDigitoVerificador(cpf, PESOS_PRIMEIRO_DIGITO);
		int segundoDigito = calcularDigitoVerificador(cpf, PESOS_SEGUNDO_DIGITO);

		return primeiroDigito == Character.getNumericValue(cpf.charAt(9))
		        && segundoDigito == Character.getNumericValue(cpf.charAt(10));
	}

	private static int calcularDigitoVerificador(String cpf, int[] pesos) {
		int soma = 0;
		for (int i = 0; i < pesos.length; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * pesos[i];
		}

		int resto = soma % 11;
		return resto < 2 ? 0 : 11 - resto;
	}
}
