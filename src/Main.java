import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/account";
        String id = "root";
        String pw = "root";

        try (var sql = new SQLWorker(url, id, pw)) {
            sql.connect();

            if(sql.isConnect()){
                sql.excute(SQLWorker.QueryType.SELECT,"SELECT ID, PW FROM USERS");
                String resultId = sql.getResult().getString("ID");
                String resultPw = sql.getResult().getString("PW");

                System.out.println(resultId + "/" + resultPw);

                String newPw = scanner.nextLine();

                if(sql.excute(SQLWorker.QueryType.UPDATE,"UPDATE USERS SET PW='" + newPw + "' WHERE ID='" + resultId + "'")){
                    sql.excute(SQLWorker.QueryType.SELECT,"SELECT ID, PW FROM USERS");

                    resultId = sql.getResult().getString("ID");
                    resultPw = sql.getResult().getString("PW");

                    System.out.println(resultId + "/" + resultPw);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}