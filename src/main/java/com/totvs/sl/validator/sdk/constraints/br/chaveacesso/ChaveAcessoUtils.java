package com.totvs.sl.validator.sdk.constraints.br.chaveacesso;

/**
 * Utilitário para extração de informações de uma Chave de Acesso de documentos
 * fiscais eletrônicos (NF-e, NFC-e, CT-e, etc.).
 *
 * <p>
 * Estrutura da Chave de Acesso (44 caracteres alfanuméricos):
 * <ul>
 * <li>Posições 1-2: Código da UF</li>
 * <li>Posições 3-6: Ano e Mês de emissão (AAMM)</li>
 * <li>Posições 7-20: CNPJ do emitente</li>
 * <li>Posições 21-22: Modelo do documento</li>
 * <li>Posições 23-25: Série</li>
 * <li>Posições 26-34: Número do documento</li>
 * <li>Posições 35-43: Código numérico</li>
 * <li>Posição 44: Dígito verificador</li>
 * </ul>
 */
public final class ChaveAcessoUtils {

	private static final int INICIO_CNPJ = 6;
	private static final int FIM_CNPJ = 20;
	private static final int INICIO_SERIE = 22;
	private static final int FIM_SERIE = 25;
	private static final int INICIO_NUMERO = 25;
	private static final int FIM_NUMERO = 34;

	private ChaveAcessoUtils() {
		// Classe utilitária, não deve ser instanciada
	}

	/**
	 * Extrai o CNPJ do emitente da Chave de Acesso (posições 7 a 20).
	 *
	 * @param chave Chave de Acesso com 44 caracteres
	 * @return CNPJ do emitente
	 */
	public static String getCnpjEmitente(String chave) {
		return chave.substring(INICIO_CNPJ, FIM_CNPJ);
	}

	/**
	 * Extrai o número do documento da Chave de Acesso (posições 26 a 34).
	 *
	 * @param chave Chave de Acesso com 44 caracteres
	 * @return Número do documento
	 */
	public static String getNumero(String chave) {
		return chave.substring(INICIO_NUMERO, FIM_NUMERO);
	}

	/**
	 * Extrai a série do documento da Chave de Acesso (posições 23 a 25).
	 *
	 * @param chave Chave de Acesso com 44 caracteres
	 * @return Série do documento
	 */
	public static String getSerie(String chave) {
		return chave.substring(INICIO_SERIE, FIM_SERIE);
	}
}
