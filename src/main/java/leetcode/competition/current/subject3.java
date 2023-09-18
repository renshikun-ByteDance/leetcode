package leetcode.competition.current;


import java.util.*;

/**
 * 2022年中国民生银行信用卡中心"第五届 1024编程大赛"
 */
public class subject3 {

    static ArrayList<Server> servers;
    static ArrayList<Application> application;

    public static void main(String[] args) {
        servers = new ArrayList<>();
        application = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        //两行信息
        String[] mainInfo = new String[2];
        int lines = 0;  //计数
        while (scanner.hasNext()) {
            mainInfo[lines] = scanner.nextLine();
            lines++;
            if (lines == 2) break;
        }

        //----------------------------------
        // 1、第一行数据预处理，服务器数据处理
        //----------------------------------
        String[] serverInfo = mainInfo[0].split("\\s+");   //服务器信息
        int serverNums = Integer.parseInt(serverInfo[0]);  //服务器个数
        int currentIndex = 1;
        int serverId = 1;
        while (currentIndex < serverInfo.length) {
            int cpu = Integer.parseInt(serverInfo[currentIndex]);
            currentIndex++;
            int mem = Integer.parseInt(serverInfo[currentIndex]);
            currentIndex++;
            Server server = new Server(serverId, cpu, mem);
            server.updateWeight();  //更新权重

            servers.add(server);  //添加
            serverId++;   //服务id自增
        }
        //----------------------------------
        // 1、应用数据预处理
        //----------------------------------
        String[] appInfo = mainInfo[1].split("\\s+");
        int appNums = Integer.parseInt(appInfo[0]);
        ArrayList<Application> applications = new ArrayList<>();
        currentIndex = 1;
        int appID = 0;
        while (currentIndex < appInfo.length) {
            int cpu = Integer.parseInt(appInfo[currentIndex]);
            currentIndex++;
            int mem = Integer.parseInt(appInfo[currentIndex]);
            currentIndex++;
            String name = appInfo[currentIndex];
            currentIndex++;

            application.add(new Application(++appID, cpu, mem, name));
        }

        //---------------------------------------------------
        // 规则逻辑处理
        //---------------------------------------------------
        ArrayList<Integer> restApp = new ArrayList<>();  //有序记录不可分配资源的服务名称
        HashMap<Integer, HashSet<String>> hTable = new HashMap<>();  //记录各个服务器当前分配的应用
        for (int i = 1; i <= serverNums; i++) {
            hTable.put(i, new HashSet<>());
        }

        //优先队列，记录各个服务的权重
        PriorityQueue<Server> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1.weight != o2.weight) {
                return o2.weight - o1.weight;   //优先按照权重，降序排序
            } else {
                return o1.id - o2.id;           //然后按照编号，升序排序
            }
        });

        //将所有的服务器添加到队列中，从而确定各个服务器被选择的顺序
        sortedQueue.addAll(servers);

        //依次处理每个服务
        for (Application app : application) {
            //记录不满足条件的服务器，后续重新添加到队列中
            ArrayList<Server> restServer = new ArrayList<>();

            int flag = 0;
            //针对单个应用，有序遍历服务器
            while (!sortedQueue.isEmpty()) {
                Server server = sortedQueue.poll();
                int id = server.id;
                //判断当前服务器是否满足被选择的条件
                if (server.cpu >= app.cpuNeed &&
                        server.mem >= app.memNeed && !hTable.get(id).contains(app.appName)) { //满足条件

                    //记录满足要求的服务器
//                    currentQueue.add(server);
                    hTable.get(id).add(app.appName);  //更新当前服务器中部署的服务

                    flag = 1;
                    //消耗集群的资源
                    server.mem -= app.memNeed;
                    server.cpu -= app.cpuNeed;
                    server.updateWeight();  //更新权重
                    sortedQueue.add(server);  //添加至队列
                    break;
                } else {
                    restServer.add(server);  //不放回原队列，放回到其他队列
                }
                //继续判断下一个服务器
            }
            //--------------------------
            // 结束循环
            //--------------------------
            if (flag == 0) {
                restApp.add(app.id);
            }
            sortedQueue.addAll(restServer);  //将当前服务不可选用的服务器，重新加入到队列
        }

        //--------------------------------------------
        // 输出信息处理
        //--------------------------------------------
        ArrayList<String> result = new ArrayList<>();
        //1、记录服务器剩余资源


        HashMap<Integer, Server> out = new HashMap<>();

        while (!sortedQueue.isEmpty()) {
            Server poll = sortedQueue.poll();
            out.put(poll.id, poll);
        }
        for (int i = 1; i <= serverNums; i++) {
            //依次记录服务器的 cpu 和 mem 信息
            Server poll = out.get(i);
            result.add(String.valueOf(poll.cpu));
            result.add(String.valueOf(poll.mem));
        }

        //2、记录未分配的应用
        for (int i = 0; i < restApp.size(); i++) {
            result.add(String.valueOf(restApp.get(i)));
        }
        System.out.println(String.join(" ", result));
    }

    public static class Server {
        int id;
        int cpu;
        int mem;
        int weight;  //权重

        Server(int id, int cpu, int mem) {
            this.id = id;
            this.cpu = cpu;
            this.mem = mem;
        }

        public void updateWeight() {
            this.weight = cpu * 2 + mem;
        }
    }

    public static class Application {
        int id;
        int cpuNeed;
        int memNeed;
        String appName;

        public Application(int id, int cpuNeed, int memNeed, String appName) {
            this.id = id;
            this.cpuNeed = cpuNeed;
            this.memNeed = memNeed;
            this.appName = appName;
        }
    }


}
