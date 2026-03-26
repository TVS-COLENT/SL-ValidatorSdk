# SL-Validator-SDK

Biblioteca com regras de validação CNPJ Alfanumérico, CNPJ Numérico, Chave Acesso com CNPJ Alfa numérico e Chave Acesso com CNPJ numérico para a Suíte Logística.

## Setup de desenvolvimento

Para desenvolvimento local é necessário:
* Java: `17`
* Maven: `3.8+`
* *Feed* (*Azure Artifactory*): `SuiteLogistica`

### Configurando a biblioteca no projeto

É necessário realizar algumas configurações para identificação da dependência no Maven:

1. No `pom.xml` do projeto é necessário adicionar:

    ```xml
    <repositories>       

    <dependencies>
        ...
        <!-- Suíte Logística Validators -->
          <dependency>
           <groupId>io.github.coletaentrega</groupId>
           <artifactId>validator-sdk</artifactId>
           <version>1.0.0</version>
         </dependency>
        ...
    </dependencies>
   
    ```
implementação das mesmas podem ser consultadas neste repositório. A listagem de todas as regras e inclusive, atualização ou inclusão de novas regras será documentada no arquivo de [CHANGELOG](./CHANGELOG.md) desta biblioteca.