package application;



import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaFXMLController implements Initializable {
	@FXML
	private Label id;
	@FXML
	private Label nome;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TableView<Aluno> tabelaAlunos;
	@FXML
	private TableColumn<Aluno, Integer> colunaId;
	@FXML
	private TableColumn<Aluno, String> nomeCol;
	
	ObservableList<Aluno> list = FXCollections.observableArrayList();
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSelect;

        
        @Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		iniciarTable();
		tabelaAlunos.setItems(list);

	}

	public void actionTexto(ActionEvent event) {
		id.setText(txtId.getText());
		nome.setText(txtNome.getText());
	}

	@FXML
	public void actionSQLInsert(ActionEvent event) {
    try {
        DBUtil db = DBUtil.getInstance();
        // Prepara a declaração SQL para inserir um novo registro na tabela aluno
        PreparedStatement ps = db.getConnection().prepareStatement("Insert into aluno (id, nome) values (?, ?)");
        // Obtém o valor digitado no campo txtId e tenta convertê-lo para um inteiro.
        String idString = txtId.getText();
        int id = 0;
        // metodo tryParseInt para tentar converter o texto em um inteiro
        try {
            id = Integer.parseInt(idString);
            // Se a conversão foi bem-sucedida, atribui o valor convertido ao primeiro parâmetro da declaração SQL.
            ps.setInt(1, id);
        } catch (NumberFormatException e) {
            // Se a conversão falhou, imprime uma mensagem de erro no console.
            System.out.println("Erro: o campo id não é um inteiro válido.");
        }
        //metodo instaceof pra retornar true caso objeto seja uma string
        if(txtNome.getText() instanceof String) {
            ps.setString(2, txtNome.getText());
        } else {
            System.out.println("Erro: o campo nome não é uma string.");
        }
        // Executa a declaração SQL.
        ps.execute();
    } catch (Exception e) {
        System.out.println("Erro: " + e.toString());
    }
     // Atualiza a tabela de exibição para refletir a nova inserção.
    iniciarTable();
}


	@FXML
	public void actionSQLSelect(ActionEvent event) {
		try {
			DBUtil db = DBUtil.getInstance();
			PreparedStatement ps = db.getConnection().prepareStatement("Select * from aluno");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "-" + rs.getString("nome"));
                                
                                  
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.toString());
		}
		iniciarTable();
	}

	@FXML
	public void actionSQLDelete(ActionEvent event) {
		try {
			DBUtil db = DBUtil.getInstance();
			PreparedStatement ps = db.getConnection().prepareStatement("Delete from aluno where id = ?");
			ps.setInt(1, Integer.parseInt(txtId.getText()));
			ps.execute();
                          
		} catch (Exception e) {
			System.out.println("Erro: " + e.toString());
		}
		iniciarTable();
	}

	@FXML
	public void actionSQLUpdate(ActionEvent event) {
		try {
			DBUtil db = DBUtil.getInstance();
			PreparedStatement ps = db.getConnection().prepareStatement("update aluno set nome = ? where id = ?");
			ps.setString(1, txtNome.getText());
			ps.setInt(2, Integer.parseInt(txtId.getText()));
			ps.execute();
                          
		} catch (Exception e) {
			System.out.println("Erro: " + e.toString());
		}
		iniciarTable();
	}
	public void iniciarTable(){
        // Limpa a lista de alunos
        list.clear();
    
        try {
        // Obtém uma instância da classe DBUtil, que gerencia a conexão com o banco de dados.
        DBUtil db = DBUtil.getInstance();
        
        // Prepara a declaração SQL para selecionar todos os registros da tabela aluno.
        PreparedStatement ps;
        ps = db.getConnection().prepareStatement("SELECT * from aluno");
        
        // Executa a declaração SQL e armazena o resultado em um ResultSet.
        ResultSet rs = ps.executeQuery();
        
        // Lê os dados de cada registro do ResultSet e adiciona-os a uma lista de objetos Aluno.
        while(rs.next()){
            list.add(new Aluno(rs.getInt("id"), rs.getString("nome")));
        }
        
        // Limpa os campos de texto após modificar a tabela
        txtId.setText("");
        txtNome.setText("");
        
    } catch (Exception e) {
        // Se ocorrer uma exceção, imprime uma mensagem de erro no console.
        System.out.println("Erro: " + e.toString());
    }
}

       @FXML
    private void onMouseClick(MouseEvent event) {
    // Obtém o objeto Aluno correspondente à linha selecionada na tabela.
    Aluno aluno = tabelaAlunos.getSelectionModel().getSelectedItem();
    
    // Define o valor do campo de texto txtId como o valor do campo "id" do objeto Aluno.
    txtId.setText(aluno.getId().toString());
    
    // Define o valor do campo de texto txtNome como o valor do campo "nome" do objeto Aluno.
    txtNome.setText(aluno.getNome());
    
    // Imprime no console os valores dos campos "id" e "nome" do objeto Aluno.
    System.out.println("id " + aluno.getId() );
    System.out.println("nome " + aluno.getNome());
}


    @FXML
    private void onMouseClick(javafx.scene.input.MouseEvent event) {
        Aluno aluno = tabelaAlunos.getSelectionModel().getSelectedItem();
        txtId.setText(aluno.getId().toString());
	txtNome.setText(aluno.getNome());
        System.out.println("id " + aluno.getId() );
        System.out.println("nome " + aluno.getNome());
    }
}