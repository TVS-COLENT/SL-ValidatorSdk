# Changelog

Todas as alterações relevantes a este projeto serão documentadas neste arquivo.

## Features

### [1.1.0] - 30-03-2026

- **Métodos estáticos para uso** — `CNPJValidator.sanitize(String)` e `CPFValidator.sanitize(String)` podem ser
  chamados diretamente sem depender do contexto Bean Validation para remover formatação de CPF e CNPJ.

### [1.0.0] - 25-03-2026

- **@CNPJ** — Annotation de validação de CNPJ (Bean Validation + método estático `CNPJValidator.isValid(String)`)
    - Suporte a CNPJ numérico e alfanumérico
    - Aceita valores formatados (`XX.XXX.XXX/XXXX-XX`)
    - Rejeita dígitos todos iguais
    - Validação por Módulo 11

- **@CPF** — Annotation de validação de CPF (Bean Validation + método estático `CPFValidator.isValid(String)`)
    - Aceita valores formatados (`XXX.XXX.XXX-XX`)
    - Rejeita dígitos todos iguais
    - Validação por Módulo 11

- **@ChaveAcesso** — Annotation de validação de Chave de Acesso de documento fiscal eletrônico

- **Internacionalização (i18n)** — Mensagens de validação em 3 idiomas:
    - Português (`pt`)
    - Inglês (`en_US`)
    - Espanhol (`es_ES`)

- **Métodos estáticos para uso em Domain** — `CNPJValidator.isValid(String)` e `CPFValidator.isValid(String)` podem ser
  chamados diretamente sem depender do contexto Bean Validation

- **Testes unitários** — Cobertura com JUnit 5 e testes parametrizados para CPF e CNPJ



