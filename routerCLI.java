import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class routerCLI {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Virtual Router CLI");

        System.out.println("Type 'Help' for commands. ");

        while (true) {
            System.out.println("router>");

            String command = sc.nextLine();
            if (command.equalsIgnoreCase("Exit")) {
                break;
            } else if (command.equalsIgnoreCase("help")) {
                System.out.println("Available commands : ");
                System.out.println("Help");
                System.out.println("Exit");
                System.out.println("Status");
                System.out.println("router-info");
            } else if (command.equalsIgnoreCase("status")) {

                System.out.println("DHCP : Active");

                System.out.println("DNS :Not Configured");

                System.out.println("Firewall :Not Configured");

                System.out.println("Logging : Not Configured");

            } else if (command.equalsIgnoreCase("router-info")) {

                String defaultInterface = "";

                Process p = Runtime.getRuntime().exec("ip route");
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("default")) {
                        String[] parts = line.split(" ");
                        System.out.println("Default Gateway :" + parts[2]);
                        defaultInterface = parts[4];
                    }

                }

                Process add = Runtime.getRuntime().exec("ip addr");
                BufferedReader ipReader = new BufferedReader(new InputStreamReader(add.getInputStream()));

                String ipline;
                boolean foundInterface = false;
                while ((ipline = ipReader.readLine()) != null) {
                    if (ipline.contains(defaultInterface + ":")) {
                        foundInterface = true;
                    }
                    if (foundInterface && ipline.trim().startsWith("inet")) {

                        String[] ipParts = ipline.trim().split("\\s+");

                        String ipAddress = ipParts[1].split("/")[0];

                        System.out.println("IP Address : " + ipAddress);

                        break;
                    }
                }
                Process macProcess = Runtime.getRuntime().exec("ip link");

                BufferedReader macReader = new BufferedReader(new InputStreamReader(macProcess.getInputStream()));

                String macLine;

                boolean foundMacInterface = false;
                while ((macLine = macReader.readLine()) != null) {
                    if (macLine.contains(defaultInterface + ":")) {
                        foundMacInterface = true;
                    }
                    if (foundMacInterface && macLine.trim().startsWith("link/ether")) {
                        String[] macParts = macLine.trim().split("\\s+");
                        System.out.println("MAC Address :" + macParts[1]);

                        break;

                    }
                }

            }

            else {
                System.out.println("Unknown command. type 'help' for available commands.");
            }
        }
        sc.close();
    }
}
