package gtp;

public class Protocolo {
	private String version = "1";
	private String name = "Sansa";
	private int versionProtocolo = 2;
	private String[] comandos = { "boardsize", "clear_board", "genmove",
			"known_command", "komi", "list_commands", "name", "play",
			"protocol_version", "quit", "ref-nodes", "ref-playouts",
			"ref-score", "version" };

	public Protocolo() {

	}

	public String procesarComando(String linea) {
		String[] token = linea.split(" ");
		String response = "";

		if (token[0].equals("protocol_version")) {
			response = "= " + versionProtocolo;
		} else if (token[0].equals("name")) {
			response = "= " + name;
		} else if (token[0].equals("version")) {
			response = "= " + this.version;
		} else if (token[0].equals("known_command")) {
			response = "= " + esSoportado(token[1]);
		} else if (token[0].equals("list_commands")) {
			response = "= " + listarComandos();
		} else if (token[0].equals("boardsize")) {
			//boardsize(Integer.parseInt(token[1]));
			response = "= ";
		} else if (token[0].equals("clear_board")) {
			//clear_board();
			response = "= ";
		} else if (token[0].equals("play")) {
			//play(token[1], token[2]);
			response = "= ";
		} else if (token[0].equals("genmove")) {
			//response = "= " + genmove(token[1]);
		} else if (token[0].equals("final_status_list")) { // kgs
			response = "= " + listadoFinal();
		} else if (token.length > 1) {
			if (token[1].equals("protocol_version")) {
				response = "=" + token[0] + " " + this.versionProtocolo;
			} else if (token[1].equals("name")) {
				response = "=" + token[0] + " " + name;
			} else if (token[1].equals("version")) {
				response = "=" + token[0] + " " + version;
			} else if (token[1].equals("known_command")) {
				response = "=" + token[0] + " " + esSoportado(token[2]);
			} else if (token[1].equals("list_commands")) {
				response = "=" + token[0] + " " + this.listarComandos();
			} else if (token[1].equals("boardsize")) {
				//boardsize(Integer.parseInt(token[2]));
				response = "=" + token[0] + " ";
			} else if (token[1].equals("clear_board")) {
				//clear_board();
				response = "=" + token[0] + " ";
			} else if (token[1].equals("play")) {
				//play(token[2], token[3]);
				response = "=" + token[0] + " ";
			} else if (token[1].equals("genmove")) {
				//response = "=" + token[0] + " " + genmove(token[2]);
			} else if (token[1].equals("final_status_list")) { // kgs
				response = "=" + token[0] + " " + this.listadoFinal();
			} else {
				response = "? " + "unknown command\n";
				// return;
			}
		} else {
			response = "? " + "unknown command\n";
			// return;
		}

		response = response + "\n\n";

		return response;
	}

	public boolean esSoportado(String command) {
		for (int i = 0; i < this.comandos.length; i++) {
			if (this.comandos[i].equals(command)) {
				return true;
			}
		}
		return false;
	}

	public String listarComandos() {
		String list = "";

		for (int i = 0; i < this.comandos.length; i++) {
			if (i == 0) {
				list += this.comandos[i];
			} else {
				list += "\n";
				list += this.comandos[i];
			}
		}
		return list;
	}

	public String listadoFinal() {
		return "";
	}

}
