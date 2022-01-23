/*
 * Copyright (c) 2022 Bernhard Haumacher et al. All Rights Reserved.
 */
package de.haumacher.wizard.ui;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.haumacher.wizard.controller.Controller;
import de.haumacher.wizard.msg.Game;
import de.haumacher.wizard.msg.GameCreated;
import de.haumacher.wizard.msg.JoinAnnounce;
import de.haumacher.wizard.msg.LeaveAnnounce;
import de.haumacher.wizard.msg.ListGamesResult;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * TODO
 *
 * @author <a href="mailto:haui@haumacher.de">Bernhard Haumacher</a>
 */
public class GameSelector extends Controller<TableView<Game>> implements EventHandler<Event> {
	
	private final Callback<TableColumn<Game, Object>, TableCell<Game, Object>> BUTTON_CELL_FACTORY = new Callback<TableColumn<Game, Object>, TableCell<Game, Object>>() {
		@Override
		public TableCell<Game, Object> call(TableColumn<Game, Object> param) {
			TableCell<Game, Object> result = new TableCell<Game, Object>() {
				Button button = new Button("Join");
				{
					button.addEventHandler(ActionEvent.ACTION, GameSelector.this);
				}
				
				@Override
				protected void updateItem(Object item, boolean empty) {
					setText(null);
					setGraphic(null);
					
					if (!empty) {
						button.setUserData(item);
						setGraphic(button);
					}
				}
			};
			return result;
		}
	};

	private static final Callback<CellDataFeatures<Game, Object>, ObservableValue<Object>> NAME_VALUE_FACTORY = new Callback<TableColumn.CellDataFeatures<Game,Object>, ObservableValue<Object>>() {
		@Override
		public ObservableValue<Object> call(CellDataFeatures<Game, Object> param) {
			return new ObservableValueBase<Object>() {
				@Override
				public Object getValue() {
					return param.getValue().getPlayers().stream().map(p -> p.getName()).collect(Collectors.joining(", "));
				}
			};
		}
	};	
	
	private static final Callback<CellDataFeatures<Game, Object>, ObservableValue<Object>> SELF_VALUE_FACTORY = new Callback<TableColumn.CellDataFeatures<Game,Object>, ObservableValue<Object>>() {
		@Override
		public ObservableValue<Object> call(CellDataFeatures<Game, Object> param) {
			return new ReadOnlyObjectWrapper<>(param.getValue());
		}
	};

	private Consumer<Game> _onSelect;	

	@FXML
	public void initialize() {
		TableColumn<Game, Object> nameColumn = (TableColumn<Game, Object>) getView().getColumns().get(0);
		nameColumn.setCellValueFactory(NAME_VALUE_FACTORY);

		TableColumn<Game, Object> buttonColumn = (TableColumn<Game, Object>) getView().getColumns().get(1);
		buttonColumn.setCellValueFactory(SELF_VALUE_FACTORY);
		buttonColumn.setCellFactory(BUTTON_CELL_FACTORY);

		getView().setUserData(this);
	}

	public void init(ListGamesResult self, Consumer<Game> onSelect) {
		_onSelect = onSelect;
		getView().getItems().setAll(self.getGames());
	}

	@Override
	public void handle(Event event) {
		Button button = (Button) event.getSource();
		Game game = (Game) button.getUserData();
		if (game != null) {
			_onSelect.accept(game);
		}
	}

	public void processJoin(JoinAnnounce self) {
		getView().getItems().replaceAll(g -> g.getGameId().equals(self.getGameId()) ? g.addPlayer(self.getPlayer()) : g);
	}

	public void processLeave(LeaveAnnounce self) {
		getView().getItems().replaceAll(g -> g.getGameId().equals(self.getGameId()) ? removePlayer(g, self.getPlayerId()) : g);
	}

	private static Game removePlayer(Game g, String playerId) {
		g.getPlayers().removeIf(p -> p.getId().equals(playerId));
		return g;
	}

	public void processCreate(GameCreated self) {
		getView().getItems().add(self.getGame());
	}

	public void removeGame(String gameId) {
		getView().getItems().removeIf(g -> g.getGameId().equals(gameId));
	}

}
