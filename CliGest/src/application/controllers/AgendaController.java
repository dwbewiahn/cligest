package application.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.ScreenManager;
import application.models.Agendamento;
import application.models.Medico;
import application.models.DAO.AgendamentosDAO;
import application.models.DAO.MedicosDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class AgendaController {
	@FXML
	private Button h800,h830,h900,h930,h1000,h1030,h1100,h1130,
					h1200,h1230,h1300,h1330,h1400,h1430,h1500,h1530,h1600,h1630,h1700,h1730;
	
	private ObservableList<Button> horarios = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<Medico> medicos;
	
	@FXML
	private DatePicker calendario;
	
	public AgendaController() {
		
	}
	
	@FXML
	public void initialize() {
		horarios.addAll(h800,h830,h900,h930,h1000,h1030,h1100,h1130,
					h1200,h1230,h1300,h1330,h1400,h1430,h1500,h1530,h1600,h1630,h1700,h1730);
		medicos.setItems(MedicosDAO.getMedicos());
		calendario.setValue(LocalDate.now());
		mudarEstadoTodosBotoes(true);
	}

	@FXML
	private void definirHorarios() {
		mudarEstadoTodosBotoes(false);
		horarios.forEach(button -> {
			button.setStyle("-fx-background-color: #33bdef");
			button.setOnAction(e -> escolherPaciente(e));
		});
		for(String horario : MedicosDAO.getHorariosMarcados(medicos.getValue().getId())) {
			for(Button button : horarios) {
					String dataHora = calendario.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "," + button.getText();
				if(dataHora.equals(horario.toString())) {
					button.setOnAction(e -> abrirAgendamento(button));
					if(AgendamentosDAO.isConfirmado(medicos.getValue().getId(), dataHora)) 
						button.setStyle("-fx-background-color: green");
					else
						button.setStyle("-fx-background-color: gray");
				}
					
			}
		}
	}
	
	private void mudarEstadoTodosBotoes(boolean estado) {
		horarios.forEach(button -> button.setDisable(estado));
	}
	
	
	private void abrirAgendamento(Button button) {
		Agendamento agendamento = AgendamentosDAO.getAgendamento(medicos.getValue().getId(),
				calendario.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "," + button.getText());
		
		AgendamentoController controller = new AgendamentoController(agendamento);
		ScreenManager.createNewWindowModal("./views/Agendamento.fxml", controller);
		
		definirHorarios();
	}
	
	@FXML
	private void escolherPaciente(Event e) {
		Button button = (Button) e.getSource();
		String dataHora = calendario.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "," + button.getText();
		ScreenManager.createNewWindowModal("./views/EscolherPaciente.fxml", new EscolherPacienteController(medicos.getValue(), dataHora));
		
		definirHorarios();
		System.out.println("Abrindo escolha paciente...");
	}
}
