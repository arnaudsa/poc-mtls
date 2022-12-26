#### Gerando Certificados Raiz
Supondo que a CLI do openssl já esteja instalada, siga as etapas abaixo para gerar um certificado raiz autoassinado. Nós o usaremos para assinar certificados de cliente e servidor posteriormente nesta história.

O comando abaixo gera uma chave privada para o certificado raiz.
```shell
openssl genrsa -des3 -out rootCA.key 2048openssl genrsa -des3 -out rootCA.key 2048
```

Solicite um certificado do openssl usando a chave gerada na etapa anterior
```shell
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 1825 -out rootCA.pem
```
![certificado raiz](docs/images/img-certificate.png)


#### Gerando Certificado de Servidor

Vamos criar uma chave privada e, em seguida, um CSR para nosso certificado de servidor.
```shell
openssl genrsa -des3 -out server.key 2048
```

Agora solicite um CSR com a chave como chave de entrada:
Ele solicitará o mesmo tipo de informação necessária para a CA raiz. O campo CN precisa ser um nome de host totalmente qualificado no qual o servidor estará acessível, neste caso, localhost
```shell
openssl req -new -sha256 -key server.key -out server.csr
```

O(s) CSR(s) deve(m) ser fornecido(s) pela entidade que solicita o certificado e, em seguida, passado para a CA raiz. Nesse caso, como também atuamos como CA raiz, vamos assinar o certificado do servidor com o CSR fornecido
```shell
openssl x509 -req -in server.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out server.pem -days 365 -sha256
```

Você será solicitado a fornecer a senha da chave da CA raiz para prosseguir e assinar o novo certificado. Isso deve criar o certificado do servidor com o nome server.pem que, junto com server.key , será usado para configurar o Tomcat para habilitar o SSL.

#### Certificado de assinatura do cliente
Conforme mencionado anteriormente, o TLS mútuo é baseado na autenticação mútua de ambas as partes. Se fosse TLS unidirecional, não precisaríamos do certificado do cliente, porque o servidor não o solicitaria. Neste caso, porém, gostaríamos que o cliente apresentasse seu certificado e que o servidor o autenticasse. Vamos criar certificados de cliente para que possamos usá-los para chamar a API.
```shell
openssl genrsa -des3 -out client.key 2048
```

Em seguida, crie um CSR para o cliente da mesma maneira
```shell
openssl req -new -sha256 -key client.key -out client.csr
```

Em seguida, assinamos o certificado do cliente também da mesma maneira
```shell
openssl x509 -req -in client.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out client.pem -days 365 -sha256
```

Excelente! Agora temos certificados de cliente e servidor. Vamos usar esses certificados para proteger nosso aplicativo de inicialização Spring.

#### Configurando o TLS no projeto
O Spring fornece um conjunto de configurações por meio das quais você pode introduzir o certificado. Ele espera o certificado e sua chave privada correspondente em um armazenamento de chaves agrupadas no formato JKS ou PKCS#12. A keytool, que é uma ferramenta para gerar armazenamentos de chaves, mudou o tipo de armazenamento padrão para PKCS12 do JDK 9+ e, aparentemente, o JKS não será mais suportado. Seguindo o mesmo caminho, também agruparemos nosso certificado de servidor e sua chave em um armazenamento de chaves PKCS12.

Navegue até o diretório onde você tem os certificados e execute o seguinte comando para criar um armazenamento de chaves a partir do certificado do servidor e sua chave privada.
```shell
openssl pkcs12 -export -in server.pem -out keystore.p12 -name server -nodes -inkey server.key
```

Isso exportará o certificado e a chave privada para um armazenamento de chaves no formato PKCS, que podemos usar para configurar o aplicativo Spring. Por padrão, a chave privada é importada para o armazenamento de chaves criptografada, caso em que precisaremos instruir o Spring a descriptografá-la usando sua senha. Para esta história, vamos descriptografar a chave privada antes de importá-la. Os nós cuidarão disso no comando acima, que solicitará a senha da chave. A CLI também solicitará uma senha para o armazenamento de chaves que precisaremos fornecer ao Spring posteriormente. Portanto, anote-o!

Agora vamos colocar o arquivo keystore.p12 gerado no diretório src/main/resources/certificates de nosso aplicativo.

Para configurar o tls no projeto devemos configurar o aplication.properties com os seguintes parametros
```shell
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:certificates/keystore.p12
server.ssl.key-store-password=1234
server.ssl.key-store-type=PKCS12
server.ssl.protocol=TLS
server.ssl.enabled-protocols=TLSv1.2
```

Observação: se a chave foi importada para o armazenamento de chaves criptografada, a senha-chave deve ser fornecida para descriptografar a chave privada.

Inicie o aplicativo e veja se o https está ativo, conforme o log abaixo
```shell
Tomcat started on port(s): 8443 (https) with context path ‘’
```

O comando abaixo também valida se o TLS está ativo
```shell
openssl s_client -connect localhost:8443
```

Com estas configurações já temos o TLS ativo no servidor, faça um teste e veja que agora é preciso acessar o endpoint usando o protocolo seguro `https`.

Se você usar um cliente de API como Postman, receberá uma resposta como a Bad Request. This combination of host and port requires TLS, que está nos dizendo para usar https em vez de http.

Parece que o Postman não pode verificar o certificado do nosso servidor durante o aperto de mão, o que faz sentido porque o Postman não tem ideia de que somos uma autoridade de certificação. Ele já confia em outras CAs públicas na Internet, mas de alguma forma precisamos fazer com que o Postman confie em nós como uma CA raiz.

Navegue até as preferências do Postman e, em seguida, na guia certificados, onde você pode adicionar certificados às suas solicitações com base no nome do host e no número da porta. Há uma seção na parte superior intitulada Certificados de CA que fornece a capacidade de confiar manualmente em uma CA. Esta é uma das etapas na configuração do TLS, unidirecional ou mútuo

Ative a alternância e selecione o arquivo rootCA.pem para que o Postman possa confiar na CA (nós). Execute novamente a solicitação e a conexão deve ser verificada agora.

#### Ativando o TLS mútuo (MTLS)
Até agora, nosso servidor de API tem sua configuração de TLS unidirecional e os clientes que confiam na CA raiz podem enviar solicitações. No entanto, para obter a autenticação mútua entre o cliente e o servidor, precisamos alterar um pouco nossa configuração para que nosso servidor também solicite o certificado do cliente. Navegue até o arquivo application.properties e adicione as seguintes propriedades:
```shell
server.ssl.client-auth=need
```
Essa chave nos permite configurar se queremos autenticação do cliente (também conhecida como autenticação mútua) ou não. Pode ser NEED , WANT e NONE . NEED indica que o servidor deve validar o certificado do cliente enquanto WANT também solicita o certificado do cliente com a principal diferença de que mantém a conexão se nenhuma autenticação for fornecida. Usando NONE, o certificado do cliente nunca é solicitado. Reinicie o aplicativo e tente o terminal novamente.

Postman não parece estar feliz com o resultado. Embora o erro seja muito curto e não seja realmente útil, se você enrolar esse endpoint com log detalhado, verá que o aperto de mão com o servidor falhou. Nesse caso, o servidor não pôde verificar o cliente porque o cliente não estava apresentando nenhum tipo de certificado e a conexão caiu durante o handshake. Se você habilitar o registro de depuração no aplicativo Spring, isso deve ficar visível como uma exceção com a mensagem `empty client certificate chain`. O que precisamos fazer para resolver isso é enviar o certificado do cliente junto com a solicitação ao chamar a API.

Navegue até a guia certificados nas preferências do postman novamente e clique em Adicionar certificado e adicione a seguinte entrada:
```shell
- host: localhost
- porta: 8443
- Arquivo CRT: /path/to/client.pem
- Arquivo KEY: /path/to/client. chave
```

Reenvie a solicitação novamente e o Postman reclama do mesmo erro. Embora o cliente e o servidor apresentem seus certificados um ao outro, a conexão ainda falha ao ser estabelecida. Se você examinar o log do seu servidor, poderá rastrear que o SslHandShakeException é gerado sempre que o cliente chama o endpoint e em algum lugar nessas linhas, há uma mensagem dizendo “unknown certificate” . Essa exceção é lançada porque nosso servidor não pode verificar o certificado do cliente porque não confia na CA raiz do certificado do cliente. Ele sabe que é um certificado válido, mas não tem ideia de onde foi assinado e, portanto, rejeita a solicitação.

Acontece que habilitar a autenticação do certificado do cliente requer confiança na CA raiz da outra parte, a menos que seja confiável na Internet. Java faz esse mecanismo de confiança com pacotes de objetos chamados Trust Store . Assim como os armazenamentos de chaves que mantêm o certificado do servidor, os armazenamentos confiáveis ​​contêm certificados nos quais o aplicativo precisa confiar para validar e autenticar clientes. O Java usa seu próprio armazenamento confiável por padrão, que é enviado com todos os JRE(s). No entanto, o Spring fornece flexibilidade suficiente para que possamos apontar um aplicativo para um armazenamento confiável da mesma forma que fizemos com o armazenamento de chaves.

Vamos criar um armazenamento confiável contendo a CA raiz que assinou o certificado do cliente. Navegue até onde estão os certificados do cliente e execute o seguinte comando:

```shel
keytool -import -file rootCA.pem -alias rootCA -keystore truststore.p12
```

Este comando usa o keytool CLI mencionado acima para criar um armazenamento confiável no formato PKCS12. Ele solicita uma senha que precisaremos ao configurar o aplicativo. A CLI mostra as informações do certificado e pergunta se deve confiar no certificado. Informe sim e crie o armazenamento confiável.

![trust-store](docs/images/trust-store.png)

Agora vamos colocar o arquivo truststore.p12 dentro da pasta src/main/resources/certificates e no arquivo application.properties vamos adicionar as seguintes propriedades:
```shell
server.ssl.trust-store=classpath:certificates/truststore.p12
server.ssl.trust-store-password=123456
server.ssl.trust-store-type=PKCS12
```

Reinicie o aplicativo e reenvie a solicitação com o Postman. Agora podemos ver que o servidor também aceitou o certificado do cliente como válido , pois confia na autoridade que o assinou.

