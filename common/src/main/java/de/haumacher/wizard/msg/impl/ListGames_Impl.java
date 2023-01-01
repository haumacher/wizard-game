package de.haumacher.wizard.msg.impl;

/**
 * Requests a listing of games waiting for player.
 */
public class ListGames_Impl extends de.haumacher.wizard.msg.impl.Cmd_Impl implements de.haumacher.wizard.msg.ListGames {

	/**
	 * Creates a {@link ListGames_Impl} instance.
	 *
	 * @see de.haumacher.wizard.msg.ListGames#create()
	 */
	public ListGames_Impl() {
		super();
	}

	@Override
	public String jsonType() {
		return LIST_GAMES__TYPE;
	}

	@Override
	public <R,A,E extends Throwable> R visit(de.haumacher.wizard.msg.Cmd.Visitor<R,A,E> v, A arg) throws E {
		return v.visit(this, arg);
	}

}
