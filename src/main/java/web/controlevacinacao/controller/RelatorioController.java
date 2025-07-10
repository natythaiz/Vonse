package web.controlevacinacao.controller;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Controller
public class RelatorioController {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioController.class);
    
    private final DataSource dataSource;
    
    public RelatorioController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @GetMapping("/relatorios/produtos")
    public ResponseEntity<byte[]> gerarRelatorioProdutos() {
        try {
            logger.info("Iniciando geração do relatório de produtos");
            
            // Carrega o template JRXML
            ClassPathResource resource = new ClassPathResource("reports/relatorio_prod.jrxml");
            if (!resource.exists()) {
                logger.error("Arquivo relatorio_prod.jrxml não encontrado");
                return ResponseEntity.notFound().build();
            }
            InputStream jrxmlStream = resource.getInputStream();
            logger.info("Template JRXML carregado com sucesso");
            
            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            logger.info("Relatório compilado com sucesso");
            
            // Obtém a conexão com o banco de dados
            Connection connection = dataSource.getConnection();
            logger.info("Conexão com banco de dados estabelecida");
            
            // Testa se há dados na tabela
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM produto");
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Encontrados {} produtos na tabela", count);
                }
            }
            
            // Parâmetros do relatório (vazio neste caso)
            Map<String, Object> parameters = new HashMap<>();
            
            // Gera o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            logger.info("Relatório preenchido com dados do banco");
            
            // Exporta para PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            logger.info("PDF gerado com sucesso. Tamanho: {} bytes", pdfBytes.length);
            
            // Fecha a conexão
            connection.close();
            
            // Configura os headers da resposta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_produtos.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório de produtos", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/relatorios/clientes")
    public ResponseEntity<byte[]> gerarRelatorioClientes() {
        try {
            logger.info("Iniciando geração do relatório de clientes");
            
            // Carrega o template JRXML
            ClassPathResource resource = new ClassPathResource("reports/relatorio_client.jrxml");
            if (!resource.exists()) {
                logger.error("Arquivo relatorio_client.jrxml não encontrado");
                return ResponseEntity.notFound().build();
            }
            InputStream jrxmlStream = resource.getInputStream();
            logger.info("Template JRXML carregado com sucesso");
            
            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            logger.info("Relatório compilado com sucesso");
            
            // Obtém a conexão com o banco de dados
            Connection connection = dataSource.getConnection();
            logger.info("Conexão com banco de dados estabelecida");
            
            // Testa se há dados na tabela
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM cliente");
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Encontrados {} clientes na tabela", count);
                }
            }
            
            // Parâmetros do relatório (vazio neste caso)
            Map<String, Object> parameters = new HashMap<>();
            
            // Gera o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            logger.info("Relatório preenchido com dados do banco");
            
            // Exporta para PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            logger.info("PDF gerado com sucesso. Tamanho: {} bytes", pdfBytes.length);
            
            // Fecha a conexão
            connection.close();
            
            // Configura os headers da resposta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_clientes.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório de clientes", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/relatorios/vendas")
    public ResponseEntity<byte[]> gerarRelatorioVendas() {
        try {
            logger.info("Iniciando geração do relatório de vendas");
            
            // Carrega o template JRXML
            ClassPathResource resource = new ClassPathResource("reports/relatorio_venda.jrxml");
            if (!resource.exists()) {
                logger.error("Arquivo relatorio_venda.jrxml não encontrado");
                return ResponseEntity.notFound().build();
            }
            InputStream jrxmlStream = resource.getInputStream();
            logger.info("Template JRXML carregado com sucesso");
            
            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            logger.info("Relatório compilado com sucesso");
            
            // Obtém a conexão com o banco de dados
            Connection connection = dataSource.getConnection();
            logger.info("Conexão com banco de dados estabelecida");
            
            // Testa se há dados na tabela
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM venda");
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Encontradas {} vendas na tabela", count);
                }
            }
            
            // Parâmetros do relatório (vazio neste caso)
            Map<String, Object> parameters = new HashMap<>();
            
            // Gera o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            logger.info("Relatório preenchido com dados do banco");
            
            // Exporta para PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            logger.info("PDF gerado com sucesso. Tamanho: {} bytes", pdfBytes.length);
            
            // Fecha a conexão
            connection.close();
            
            // Configura os headers da resposta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_vendas.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório de vendas", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/relatorios/teste")
    public ResponseEntity<String> testarConexao() {
        try {
            Connection connection = dataSource.getConnection();
            logger.info("Conexão com banco de dados estabelecida com sucesso");
            connection.close();
            return ResponseEntity.ok("Conexão com banco de dados OK");
        } catch (Exception e) {
            logger.error("Erro ao conectar com banco de dados", e);
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
    
    @GetMapping("/relatorios/debug")
    public ResponseEntity<String> debugRelatorio() {
        try {
            logger.info("Iniciando debug do relatório");
            
            // Testa se o arquivo existe
            ClassPathResource resource = new ClassPathResource("reports/relatorio_prod.jrxml");
            if (!resource.exists()) {
                return ResponseEntity.ok("ERRO: Arquivo relatorio_prod.jrxml não encontrado");
            }
            
            // Testa conexão com banco
            Connection connection = dataSource.getConnection();
            
            // Testa se a tabela existe e tem dados
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM produto");
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Encontrados {} produtos na tabela", count);
                }
            }
            
            // Testa a consulta do relatório
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT id, nome, categoria, tipo, preco_custo, preco_venda, estoque, descricao, fornecedor, status FROM produto LIMIT 1");
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    logger.info("Primeiro produto encontrado: {}", nome);
                }
            }
            
            connection.close();
            
            return ResponseEntity.ok("DEBUG: Arquivo encontrado, conexão OK, tabela existe");
            
        } catch (Exception e) {
            logger.error("Erro no debug", e);
            return ResponseEntity.internalServerError().body("ERRO: " + e.getMessage());
        }
    }
    
    @GetMapping("/relatorios/dados")
    public ResponseEntity<String> verificarDados() {
        try {
            Connection connection = dataSource.getConnection();
            StringBuilder result = new StringBuilder();
            
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT id, nome, categoria, tipo, preco_custo, preco_venda, estoque, descricao, fornecedor, status FROM produto LIMIT 5");
                
                result.append("Produtos encontrados:\n");
                while (rs.next()) {
                    result.append(String.format("ID: %d, Nome: %s, Categoria: %s, Tipo: %s\n", 
                        rs.getInt("id"), 
                        rs.getString("nome"), 
                        rs.getString("categoria"), 
                        rs.getString("tipo")));
                }
            }
            
            connection.close();
            return ResponseEntity.ok(result.toString());
            
        } catch (Exception e) {
            logger.error("Erro ao verificar dados", e);
            return ResponseEntity.internalServerError().body("ERRO: " + e.getMessage());
        }
    }
    
    @GetMapping("/relatorios/teste-pdf")
    public ResponseEntity<byte[]> testePDF() {
        try {
            logger.info("Gerando PDF de teste");
            
            // Cria um PDF simples usando iText
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, baos);
            
            document.open();
            document.add(new com.lowagie.text.Paragraph("Teste de PDF"));
            document.add(new com.lowagie.text.Paragraph("Se você está vendo isso, a geração de PDF está funcionando!"));
            document.close();
            
            byte[] pdfBytes = baos.toByteArray();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "teste.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Erro ao gerar PDF de teste", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 