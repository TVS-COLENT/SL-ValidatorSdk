package com.totvs.sl.validator.sdk.constraints.br;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.totvs.sl.validator.sdk.constraints.br.cpf.CPFValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CPFValidator")
class CPFValidatorTest {

	private CPFValidator validator;

	@BeforeEach
	void setUp() {
		validator = new CPFValidator();
	}

	@Nested
	@DisplayName("Deve retornar true")
	class DeveRetornarTrue {

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("quando o valor é nulo ou vazio (Bean Validation)")
		void quandoValorNuloOuVazio(String value) {
			assertTrue(validator.isValid(value, null));
		}

		@Test
		@DisplayName("quando CPF sem formatação é válido")
		void quandoCpfSemFormatacaoValido() {
			assertTrue(CPFValidator.isValid("52998224725"));
		}

		@Test
		@DisplayName("quando CPF com formatação é válido")
		void quandoCpfComFormatacaoValido() {
			assertTrue(CPFValidator.isValid("529.982.247-25"));
		}

		@ParameterizedTest
		@ValueSource(strings = {
				"11144477735",
				"111.444.777-35",
				"38427642008",
				"384.276.420-08",
		})
		@DisplayName("quando CPFs válidos conhecidos")
		void quandoCpfsValidosConhecidos(String cpf) {
			assertTrue(CPFValidator.isValid(cpf));
		}
	}

	@Nested
	@DisplayName("Deve retornar false")
	class DeveRetornarFalse {

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = { "   ", "\t", "\n" })
		@DisplayName("quando o valor é nulo, vazio ou em branco (método estático)")
		void quandoValorNuloVazioOuBranco(String value) {
			assertFalse(CPFValidator.isValid(value));
		}

		@ParameterizedTest
		@ValueSource(strings = {
				"1234567",        // Muito curto
				"123456789012",   // Muito longo
				"123",            // 3 dígitos
		})
		@DisplayName("quando CPF tem tamanho incorreto")
		void quandoCpfTamanhoIncorreto(String cpf) {
			assertFalse(CPFValidator.isValid(cpf));
		}

		@ParameterizedTest
		@ValueSource(strings = {
				"00000000000",
				"11111111111",
				"22222222222",
				"33333333333",
				"44444444444",
				"55555555555",
				"66666666666",
				"77777777777",
				"88888888888",
				"99999999999",
		})
		@DisplayName("quando todos os dígitos são iguais")
		void quandoTodosDigitosIguais(String cpf) {
			assertFalse(CPFValidator.isValid(cpf));
		}

		@Test
		@DisplayName("quando dígitos verificadores são inválidos")
		void quandoDigitosVerificadoresInvalidos() {
			assertFalse(CPFValidator.isValid("52998224799"));
		}

		@Test
		@DisplayName("quando CPF contém letras")
		void quandoCpfContemLetras() {
			assertFalse(CPFValidator.isValid("5299822472A"));
		}

		@Test
		@DisplayName("quando CPF contém caracteres especiais")
		void quandoCpfContemCaracteresEspeciais() {
			assertFalse(CPFValidator.isValid("5299822@725"));
		}
	}
}

