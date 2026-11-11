
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class routerCLI {

    public static String executeCommand(String command) throws IOException {
        Process process = new ProcessBuilder("bash", "-c", command)
                .redirectErrorStream(true).start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return output.toString().trim();
    }

    public static String value(String command, String fallback)
            throws IOException {
        String result = executeCommand(command);
        return result.isEmpty() ? fallback : result;
    }

    public static String checkConfig(String path) {
        return new File(path).exists() ? "Configured" : "Not Configured";
    }

    public static void showStatus() {
        System.out.println("\n========== MODULE CONFIGURATION ==========");
        System.out.printf("%-14s : %s%n", "DHCP",
                checkConfig("config/dhcp/ap-dhcp.conf"));
        System.out.printf("%-14s : %s%n", "DNS",
                checkConfig("config/dns/dns.conf"));
        System.out.printf("%-14s : %s%n", "Firewall",
                checkConfig("config/firewall/firewall-rules.v4"));
        System.out.printf("%-14s : %s%n", "Logging",
                checkConfig("config/logging/firewall-logging.v4"));
        System.out.println("==========================================\n");
    }

    public static void showRuntimeStatus() throws IOException {
        System.out.println("\n============= LIVE ROUTER STATUS =============");

        System.out.printf("%-16s : %s%n", "Hostapd",
                value("pgrep -x hostapd >/dev/null && echo Running || echo Stopped", "Unknown"));

        System.out.printf("%-16s : %s%n", "DHCP/DNS",
                value("pgrep -x dnsmasq >/dev/null && echo Running || echo Stopped", "Unknown"));

        System.out.printf("%-16s : %s%n", "Access Point",
                value("iw dev ap0 info 2>/dev/null | grep -q 'type AP' && echo Active || echo Inactive", "Unknown"));

        System.out.printf("%-16s : %s%n", "IP Forwarding",
                value("sysctl -n net.ipv4.ip_forward", "Unknown")
                        .equals("1") ? "Enabled" : "Disabled");

        System.out.printf("%-16s : %s%n", "Firewall",
                value("iptables -nL VR_INPUT >/dev/null 2>&1 && echo Loaded || echo Not Loaded", "Unknown"));

        System.out.println("===============================================\n");
    }

    public static void showRouterInfo() throws IOException {
        System.out.println("\n========== VIRTUAL ROUTER NETWORK ==========");

        String route = executeCommand("ip route show default");
        String uplink = "";
        String gateway = "Not available";

        for (String line : route.split("\n")) {
            String[] parts = line.trim().split("\\s+");

            for (int i = 0; i < parts.length - 1; i++) {
                if (parts[i].equals("dev")) uplink = parts[i + 1];
                if (parts[i].equals("via")) gateway = parts[i + 1];
            }
            if (!uplink.isEmpty()) break;
        }

        System.out.println("\n[ INTERNET UPLINK ]");
        System.out.println("Interface       : " + (uplink.isEmpty() ? "Not found" : uplink));

        if (!uplink.isEmpty()) {
            System.out.println("IP Address      : " +
                    value("ip -o -4 addr show dev " + uplink +
                    " | awk '{print $4}'", "Not available"));

            System.out.println("Gateway         : " + gateway);

            System.out.println("MAC Address     : " +
                    value("cat /sys/class/net/" + uplink + "/address", "Not available"));
        }

        System.out.println("\n[ VIRTUAL ACCESS POINT ]");

        System.out.println("Interface       : ap0");
        System.out.println("SSID            : " +
                value("iw dev ap0 info 2>/dev/null | awk '/ssid/ {print $2}'",
                        "Not available"));

        System.out.println("AP Address      : " +
                value("ip -o -4 addr show dev ap0 | awk '{print $4}'",
                        "Not configured"));

        System.out.println("DHCP Pool       : " +
                value("grep '^dhcp-range=' config/dhcp/ap-dhcp.conf | cut -d= -f2",
                        "Not available"));

        System.out.println("Subnet          : 192.168.50.0/24");
        System.out.println("Router Gateway  : 192.168.50.1");

        System.out.println("\n[ PACKET FORWARDING ]");
        System.out.println("IPv4 Forwarding : " +
                (value("sysctl -n net.ipv4.ip_forward", "0").equals("1")
                        ? "Enabled" : "Disabled"));

        System.out.println("=============================================\n");
    }

    public static void showHelp() {
        System.out.println("\n============== AVAILABLE COMMANDS ==============");
        System.out.println("help          - Display available commands");
        System.out.println("status        - Show module configuration");
        System.out.println("runtime-status- Show live router service status");
        System.out.println("router-info   - Show uplink and access point details");
        System.out.println("exit          - Exit the CLI");
        System.out.println("================================================\n");
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n+------------------------------------------+");
        System.out.println("|        SOFTWARE-BASED VIRTUAL ROUTER     |");
        System.out.println("|              MANAGEMENT CLI             |");
        System.out.println("+------------------------------------------+");
        System.out.println("Type 'help' to view available commands.");

        while (true) {
            System.out.print("\nrouter> ");

            if (!sc.hasNextLine()) break;

            String command = sc.nextLine().trim().toLowerCase();

            switch (command) {
                case "help":
                    showHelp();
                    break;

                case "status":
                    showStatus();
                    break;

                case "runtime-status":
                    showRuntimeStatus();
                    break;

                case "router-info":
                    showRouterInfo();
                    break;

                case "exit":
                    System.out.println("Exiting Virtual Router CLI...");
                    sc.close();
                    return;

                case "":
                    break;

                default:
                    System.out.println("Unknown command. Type 'help'.");
            }
        }

        sc.close();
    }
}
