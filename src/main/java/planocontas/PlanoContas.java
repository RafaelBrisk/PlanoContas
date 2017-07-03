package planocontas;

public class PlanoContas {
	
	public static void main(String[] args) {
		ContaDao dao = new ContaDao();
		dao.getAllTree(1L).forEach(c -> {
			StringBuilder sb = new StringBuilder();
			sb.append("id: ");
			sb.append(c.getId());
			sb.append(" pai: ");
			
			if (c.getPai() == null) {
				sb.append(c.getPai());				
			} else {
				sb.append(c.getPai().getId());
			}
			System.out.println(sb.toString());
		});
	}

}
