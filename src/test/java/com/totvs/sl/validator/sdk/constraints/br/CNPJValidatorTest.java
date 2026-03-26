package com.totvs.sl.validator.sdk.constraints.br;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.totvs.sl.validator.sdk.constraints.br.cnpj.CNPJValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("CNPJValidator")
class CNPJValidatorTest {

	private CNPJValidator validator;

	@BeforeEach
	void setUp() {
		validator = new CNPJValidator();
	}

	@Nested
	@DisplayName("Deve retornar true")
	class DeveRetornarTrue {

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("quando o valor é nulo ou vazio")
		void quandoValorNuloOuVazio(String value) {
			assertTrue(validator.isValid(value, null));
		}

		@Test
		@DisplayName("quando CNPJ numérico tradicional é válido")
		void quandoCnpjNumericoTradicionalValido() {
			assertTrue(validator.isValid("11222333000181", null));
		}

		@Test
		@DisplayName("quando CNPJ numérico com formatação é válido")
		void quandoCnpjNumericoComFormatacaoValido() {
			assertTrue(validator.isValid("11.222.333/0001-81", null));
		}

		@Test
		@DisplayName("quando CNPJ alfanumérico é válido")
		void quandoCnpjAlfanumericoValido() {
			assertTrue(validator.isValid("6YAJTGCRL89J40", null));
		}

	}

	@Nested
	@DisplayName("Deve retornar false")
	class DeveRetornarFalse {

		@Test
		@DisplayName("quando CNPJ tem tamanho incorreto")
		void quandoCnpjTamanhoIncorreto() {
			assertFalse(validator.isValid("1234567890", null));
		}

		@Test
		@DisplayName("quando CNPJ tem mais de 14 caracteres sem formatação")
		void quandoCnpjMaiorQue14Caracteres() {
			assertFalse(validator.isValid("123456789012345", null));
		}

		@Test
		@DisplayName("quando CNPJ contém caracteres especiais inválidos")
		void quandoCnpjContemCaracteresEspeciais() {
			assertFalse(validator.isValid("12@BC34501DE35", null));
		}

		@Test
		@DisplayName("quando todos os caracteres são iguais")
		void quandoTodosCaracteresIguais() {
			assertFalse(validator.isValid("00000000000000", null));
			assertFalse(validator.isValid("11111111111111", null));
		}

		@Test
		@DisplayName("quando dígitos verificadores são inválidos")
		void quandoDigitosVerificadoresInvalidos() {
			assertFalse(validator.isValid("11222333000199", null));
		}

		@Test
		@DisplayName("quando dígitos verificadores contêm letras")
		void quandoDigitosVerificadoresContemLetras() {
			assertFalse(validator.isValid("12ABC34501DEAB", null));
		}

		@Test
		@DisplayName("quando CNPJ contém caracteres minúsculos")
		void quandoCnpjContemMinusculos() {
			assertTrue(validator.isValid("12abc34501de35", null));
		}
	}
}

