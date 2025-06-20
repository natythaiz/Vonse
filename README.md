# VONSE DASHBOARD (E-COMMERCE)
OBS: O código é construído principalmente no Linux Mint, mas, há a possibilidade do uso no Windows: 1. Instale o Java (JRE e JDK), 2. Instale o Apache Maven (qualquer vídeo no YouTube já te ajuda, como esse aqui: https://www.youtube.com/watch?v=-ucX5w8Zm8s), 3. Não há necessidade do uso do Docker no Windows, contanto que o banco de dados no PostgreSQL seja criado com o mesmo nome do código (controlevacinacao).

## Para usar o código (instalações no Linux, pelo terminal):
### Java

1) Se quiser, você pode desinstalar a versão anterior do Java (instalada sem o SDKMan):  
   __sudo apt purge openjdk-VERSAOAREMOVER*__

2) Instalar o SDKMan (gerenciador de instalações):  
__curl -s "https://get.sdkman.io" | bash__  
  2.1) Feche o terminal e abra um novo
   
3) Instalar o Java 23:  
4) __sdk install java 23.0.2-tem__

5) Testar a instalação:  
__java --version__  
__javac --version__

6) A instalação fica em:  
__$HOME/.sdkman/candidates/java/Versao_Instalada__

7) Se tiver mais de um Java instalado com o SDKMan você pode escolher qual usar com:  
__sdk use java Versao_Desejada__

### Maven
1) Se ainda não tem instalado o SDKMan:  
__curl -s "https://get.sdkman.io" | bash__

2) Instalar o Maven:  
__sdk install maven 3.9.9__  
  2.1) Feche a janela do terminal e abra uma nova

3) Teste a instalação com o seguinte comando no terminal:  
__mvn --version__  
  3.1) Você deve ver algo como:  
    __Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)  
    Maven home: /home/rafael/.sdkman/candidates/maven/current  
    Java version: 23.0.2, vendor: Eclipse Adoptium, runtime: /home/rafael/.sdkman/candidates/java/23.0.2-tem  
    Default locale: en_US, platform encoding: UTF-8  
    OS name: "linux", version: "6.8.0-52-generic", arch: "amd64", family: "unix"__

4) A instalação fica em:  
__$HOME/.sdkman/candidates/maven/Versao_Instalada__

### VSCode
1) Baixar e instalar o Visual Studio Code (o arquivo é o com a extensão .deb em __https://code.visualstudio.com/__)

2) Desabilitar a telemetria da Microsoft:  
File → Preferences → Settings → Buscar por "telemetry"  
Telemetry: Telemetry Level → Mudar o valor para off

3) Instalar as extensões no VSCode  
  3.1) Extension Pack for Java (Microsoft)  
  3.2) XML (Red Hat)  
  3.3) Spring Boot Extension Pack (VMware)  
  Reinicie o VSCode depois que instalar as extensões

4) Configurar o caminho do Maven no VSCode  
File → Preferences → Settings → Buscar por "maven.executable.path"  
Colocar o valor __~/.sdkman/candidates/maven/current/bin/mvn (ou o diretório do bin/mvn onde você instalou o Maven no Windows)__  
  4.1) Se você instalar o Red Hat Dependency Analytics (o VSCode sugere isso quando você abre um projeto Java nele pela 1ª vez) vai precisar configurar o caminho do Maven nele também:  
    File → Preferences → Settings → Buscar por "red hat dependecy analytics mvn.executable.path"  
    Colocar o valor __~/.sdkman/candidates/maven/current/bin/mvn__

5) Habilitar a exibição do ícone no menu:  
__mkdir /home/SEU_USUARIO/.local/share/icons__  
__sudo ln -sf "/usr/share/icons/Mint-Y/apps/256/visual-studio-code.png" "/home/SEU_USUARIO/.local/share/icons/code.png"__

### PostgreSQL (via Docker)
1) Instalar o Docker  
__sudo apt install -y apt-transport-https ca-certificates curl software-properties-common gnupg lsb-release__  
__curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg__  
__sudo chmod a+r /usr/share/keyrings/docker-archive-keyring.gpg__  
__echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu "$(. /etc/os-release && echo "$UBUNTU_CODENAME")" stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null__  
__sudo apt update && sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin__

2) Instalar o Portainer via Docker
__sudo docker volume create portainer_data__  
__sudo docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:latest__  
  2.1) Acesse o endereço https://localhost:9443  
  2.2) Na 1ª execução você vai cadastrar a senha do admin  
  2.3) Selecione o botão com o desenho da baleia com containers em cima do lado esquerdo

3) Instalar o PostgreSQL via Docker  
  3.1) Crie um diretório chamado postgresql (por exemplo dentro de Programas)  
    __cd ~/Programas__  
    __mkdir postgresql__  
    __cd postgresql__  
  3.2) Crie um arquivo chamado Dockerfile com o seguinte conteúdo:  
    __xed Dockerfile__  

(cola isso dentro do arquivo, sem as aspas):  
"# Version: 1.0  
FROM postgres:latest  
ENV REFRESHED_AT 2022-07-30  
RUN apt-get update && apt-get install -y locales  
RUN touch /usr/share/locale/locale.alias  
ENV LANG pt_BR.UTF-8  
ENV LANGUAGE pt_BR:pt  
ENV LC_ALL pt_BR.UTF-8  
RUN sed -i '/pt_BR.UTF-8/s/^# //g' /etc/locale.gen && locale-gen && update-locale LANG=pt_BR.UTF-8  
ENV TZ=America/Sao_Paulo  
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone  
HEALTHCHECK --interval=10s --timeout=5s --retries=3 CMD sh -c 'pg_isready'"  


  3.3) Criar a imagem do PostgreSQL  
    __sudo docker build -t="web/postgresql" .__
  3.4) Criar o container do PostgreSQL  
    __sudo docker volume create --name postgresql_data__  
    __sudo docker run -i -t -d --name postgresql -p 5432:5432 \-e POSTGRES_USER=postgres \-e POSTGRES_PASSWORD=12345 \--volume postgresql_data:/var/lib/postgresql/data \web/postgresql__

4) Instalar o pgAdmin4 como aplicação desktop  
__curl -fsS https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo gpg --dearmor -o /usr/share/keyrings/packages-pgadmin-org.gpg__  
__sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/packages-pgadmin-org.gpg] https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/noble pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list && apt update'__  
__sudo apt update && sudo apt install -y pgadmin4-desktop__  
  4.1) Abra o pgAdmin4  
  4.2) Adicione o servidor do PostgreSQL clicando em Add Server

5) Feito isso, dê esses comandos no terminal na pasta onde o projeto estará:  
__git clone https://github.com/natythaiz/Vonse.git__  
__cd Vonse__  
  5.1) Se quiser editar e postar no git novamente, edite e faça:  
   __git add .__  
   __git commit -m "Comentário do commit"__  
   __git push origin main__  
  5.2) Se alguém da equipe fez alguma alteração e você quiser puxar o que foi alterado, faça:  
   __git pull origin main__
