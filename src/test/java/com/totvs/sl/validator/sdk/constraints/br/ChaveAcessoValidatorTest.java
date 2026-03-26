package com.totvs.sl.validator.sdk.constraints.br;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.totvs.sl.validator.sdk.constraints.br.chaveacesso.ChaveAcessoUtils;
import com.totvs.sl.validator.sdk.constraints.br.chaveacesso.ChaveAcessoValidator;

@DisplayName("ChaveAcessoValidator")
class ChaveAcessoValidatorTest {

	private static final String CHAVE_NUMERICA_VALIDA = "42220510832644000108550110000641801229879738";
	private static final String CHAVE_ALFANUMERICA_VALIDA = "3525016YAJTGCRL89J40570010000000011000000014";

	private ChaveAcessoValidator validator;

	@BeforeEach
	void setUp() {
		validator = new ChaveAcessoValidator();
	}

	@Nested
	@DisplayName("isValid (Bean Validation) - Deve retornar true")
	class BeanValidationDeveRetornarTrue {

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("quando o valor é nulo ou vazio")
		void quandoValorNuloOuVazio(String value) {
			assertTrue(validator.isValid(value, null));
		}

		@Test
		@DisplayName("quando chave de acesso numérica é válida")
		void quandoChaveNumericaValida() {
			assertTrue(validator.isValid(CHAVE_NUMERICA_VALIDA, null));
		}
	}

	@Nested
	@DisplayName("isValid (estático) - Deve retornar true para chaves válidas")
	class ChavesValidas {

		@Test
		@DisplayName("quando chave de acesso numérica é válida")
		void quandoChaveNumericaValida() {
			assertTrue(ChaveAcessoValidator.isValid(CHAVE_NUMERICA_VALIDA));
		}

		@Test
		@DisplayName("quando chave de acesso alfanumérica é válida")
		void quandoChaveAlfanumericaValida() {
			assertTrue(ChaveAcessoValidator.isValid(CHAVE_ALFANUMERICA_VALIDA));
		}
	}

	@Nested
	@DisplayName("isValid (estático) - Deve retornar false para chaves inválidas")
	class ChavesInvalidas {

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = { "   ", "\t", "\n" })
		@DisplayName("quando o valor é nulo, vazio ou em branco")
		void quandoValorNuloVazioOuBranco(String value) {
			assertFalse(ChaveAcessoValidator.isValid(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "3506052682240600011855001000000001", "350605268224060001185500100000000110851884361",
		        "123", })
		@DisplayName("quando o tamanho é incorreto")
		void quandoTamanhoIncorreto(String chave) {
			assertFalse(ChaveAcessoValidator.isValid(chave));
		}

		@Test
		@DisplayName("quando contém caracteres especiais")
		void quandoContemCaracteresEspeciais() {
			assertFalse(ChaveAcessoValidator.isValid("3506052682240600011855001000000001@085188436"));
		}

		@Test
		@DisplayName("quando o CNPJ do emitente é inválido")
		void quandoCnpjEmitenteInvalido() {
			assertFalse(ChaveAcessoValidator.isValid("35060500000000000018550010000000011085188436"));
		}

		@Test
		@DisplayName("quando o dígito verificador é inválido")
		void quandoDigitoVerificadorInvalido() {
			assertFalse(ChaveAcessoValidator.isValid("35060526822406000118550010000000011085188439"));
		}
	}

	@Nested
	@DisplayName("ChaveAcessoUtils - Métodos utilitários")
	class MetodosUtilitarios {

		@DisplayName("deve retornar CNPJ da chave")
		void deveRetornarCnpjDaChave() {
			assertEquals("10832644000108", ChaveAcessoUtils.getCnpjEmitente(CHAVE_NUMERICA_VALIDA));
		}

		@Test
		@DisplayName("deve extrair o número do documento")
		void deveExtrairNumero() {
			assertEquals("000064180", ChaveAcessoUtils.getNumero(CHAVE_NUMERICA_VALIDA));
		}

		@Test
		@DisplayName("deve extrair a série do documento")
		void deveExtrairSerie() {
			assertEquals("011", ChaveAcessoUtils.getSerie(CHAVE_NUMERICA_VALIDA));
		}
	}
}
