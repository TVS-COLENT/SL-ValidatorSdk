package com.totvs.sl.validator.sdk.constraints.br.chaveacesso;

import org.apache.commons.lang3.StringUtils;

import com.totvs.sl.validator.sdk.constraints.br.cnpj.CNPJValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator para Chave de Acesso de documentos fiscais eletrônicos (NF-e,
 * NFC-e, CT-e, etc.).
 *
 * <p>
 * Regras:
 * <ul>
 * <li>Comprimento total: 44 caracteres alfanuméricos</li>
 * <li>Caracteres permitidos: A-Z (maiúsculos) e 0-9</li>
 * <li>O CNPJ do emitente (posições 7 a 20) deve ser válido</li>
 * <li>O último dígito é verificador, calculado pelo Módulo 11 com suporte
 * alfanumérico</li>
 * </ul>
 *
 * @see ChaveAcessoUtils para métodos utilitários de extração de dados da chave
 */
public class ChaveAcessoValidator implements ConstraintValidator<ChaveAcesso, String> {

	private static final int TAMANHO_CHAVE = 44;
	private static final int ASCII_ZERO = 48;

	/**
	 * Valida uma Chave de Acesso de forma isolada, sem depender do contexto de Bean
	 * Validation.
	 *
	 * @param value Chave de Acesso a ser validada (44 caracteres alfanuméricos)
	 * @return {@code true} se a chave for válida, {@code false} caso contrário.
	 *         Retorna {@code false} para valores nulos ou em branco.
	 */
	public static boolean isValid(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}

		String chave = value.toUpperCase();

		if (chave.length() != TAMANHO_CHAVE) {
			return false;
		}

		if (!possuiCaracteresValidos(chave)) {
			return false;
		}

		if (!validarCnpjEmitente(chave)) {
			return false;
		}

		return validarDigitoVerificador(chave);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		return isValid(value);
	}

	static boolean validarCnpjEmitente(String chave) {
		var cnpjEmitente = ChaveAcessoUtils.getCnpjEmitente(chave);
		return CNPJValidator.isValid(cnpjEmitente);
	}

	static boolean validarDigitoVerificador(String chave) {
		return calcularDigitoVerificador(chave) == getValorDigitoVerificador(chave);
	}

	private static boolean possuiCaracteresValidos(String chave) {
		for (int i = 0; i < chave.length(); i++) {
			char c = chave.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}

	// Regra do Modulo11 com suporte a caracteres alfanuméricos (CNPJ alfanumérico)
	private static int calcularDigitoVerificador(String chave) {
		var variacao = 4;
		var totalizador = 0;
		var chaveSemDigito = chave.substring(0, 43).toCharArray();

		for (char caractere : chaveSemDigito) {
			totalizador += variacao-- * converterParaValorAscii(caractere);
			if (variacao < 2) {
				variacao = 9;
			}
		}

		totalizador = totalizador % 11;
		if (totalizador == 0 || totalizador == 1) {
			return 0;
		} else {
			return 11 - totalizador;
		}
	}

	/**
	 * Converte o caractere para valor numérico baseado na tabela ASCII.
	 * <p>
	 * Para dígitos (0-9): retorna o valor numérico (0-9). Para letras (A-Z):
	 * retorna o valor ASCII menos 48 (A=17, B=18, ..., Z=42).
	 */
	private static int converterParaValorAscii(char c) {
		return Character.toUpperCase(c) - ASCII_ZERO;
	}

	private static int getValorDigitoVerificador(String chave) {
		try {
			var digitoVerificador = chave.substring(43);
			return Integer.parseInt(digitoVerificador);
		} catch (Exception e) {
			return -1;
		}
	}
}
