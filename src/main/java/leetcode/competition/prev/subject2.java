package leetcode.competition.prev;

import java.util.*;

public class subject2 {

    public static void main(String[] args) {
        //记录各个会议所属的会议室
        HashMap<Integer, Integer> meetAndRoom = new HashMap<>();
        //记录会议室选取的优先级
        PriorityQueue<Room> sortedRooms = new PriorityQueue<>((o1, o2) -> {
            if (o1.meetings.size() != o2.meetings.size()) {
                return o2.meetings.size() - o1.meetings.size();   //1、优先按照会议室中会议的数量来降序排序，用于区分会议室中是否有会议，可优化
            }
            return o1.id - o2.id;  //2、会议室内会议数量一致，则按照会议室编号升序排序
        });
        Scanner scanner = new Scanner(System.in);
        //会议室的数量
        int n = Integer.parseInt(scanner.nextLine());
        //初始化
        for (int i = 1; i <= n; i++) {
            sortedRooms.add(new Room(i, new ArrayList<Meeting>()));
        }
        int lineNum = 1;
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("-1")) break;
            String[] split = nextLine.split(",");
            int meetingID = Integer.parseInt(split[0]);
            int operator = Integer.parseInt(split[1]);  //操作类型
            if (operator == 0) {  //1、新增会议
                int startTime = Integer.parseInt(split[2]);
                int endTime = Integer.parseInt(split[3]);
                int roomID = 0;
                //获取当前会议可分配的会议室编号
                if (lineNum == 1) {  //剪枝
                    roomID = 1;
                } else {
                    PriorityQueue<Room> currentRooms = new PriorityQueue<>((o1, o2) -> {  //时间复杂度高的原因
                        if (o1.meetings.size() != o2.meetings.size()) {
                            return o2.meetings.size() - o1.meetings.size();
                        }
                        return o1.id - o2.id;
                    });
                    currentRooms.addAll(sortedRooms);
                    roomID = getRoomID(currentRooms, startTime, endTime);
                }
                //向会议室中添加会议
                addMeeting(sortedRooms, roomID, new Meeting(meetingID, startTime, endTime));
                //维护会议与会议室的所属关系
                if (roomID != -1) meetAndRoom.put(meetingID, roomID);
                System.out.println(roomID);
            } else if (operator == 1) {   //2、删除会议
                int roomID = meetAndRoom.get(meetingID);  //获取会议室编号
                cancelMeeting(sortedRooms, roomID, meetingID);    //取消此会议
                meetAndRoom.remove(meetingID);
                System.out.println(roomID);
            } else {                       //3、更新会议时间
                int startTime = Integer.parseInt(split[2]);
                int endTime = Integer.parseInt(split[3]);
                int roomID = meetAndRoom.get(meetingID);
                updateMeeting(sortedRooms, roomID, meetingID, startTime, endTime);
                System.out.println(roomID);
            }
            lineNum++;
        }
    }

    private static int getRoomID(PriorityQueue<Room> sortedRooms, int startTime, int endTime) {
        //优先队列，记录当前会议可加入的会议室
        PriorityQueue<int[]> satisfyQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[1] != o2[1]) return o1[1] - o2[1];   //先按照后续会议开始最早的会议室
            return o1[0] - o2[0];                       //开始时间相同，则按照会议室编号排序
        });

        while (!sortedRooms.isEmpty()) {  //按会议室可被选取的优先级，顺序遍历会议室
            Room room = sortedRooms.poll();
            int roomId = room.id;
            ArrayList<Meeting> meetings = room.meetings;  //会议室中的会议，也是按照先后顺序来存储
            int nums = meetings.size();

            //------------------------------------------
            // 提前剪枝、降低时间复杂度
            //------------------------------------------
            if (nums == 0) {   //由于排序的原因，找到首个没有会议的会议室，则代表有会议的会议室均处理完毕
                //判断是否有可插入的会议室
                if (!satisfyQueue.isEmpty()) {      //1、有满足条件的会议室，直接返回满足插入及其最值条件的会议室编号
                    return satisfyQueue.peek()[0];
                } else {                            //2、当前有会议的会议室，不能插入，同时存在空闲会议室，则写入到编号最小的一个
                    return roomId;
                }
            }

            //---------------------------------------------------------
            // 仅仅处理有会议的会议室的情况，拍查是否可插入这些会议室的内部
            //---------------------------------------------------------
            //头
            if (endTime <= meetings.get(0).startTime) {  //按照开始时间升序排序
                //1、有后续会议的会议室（优先级 I高）
                satisfyQueue.add(new int[]{roomId, meetings.get(0).startTime});  //能插入当前会议室的头部，记录此会议室编号，即下个会议的开始时间
            } else if (startTime >= meetings.get(meetings.size() - 1).endTime) { //尾，能处理的会议室中一定有会议
                //2、没有后续会议的会议室（优先级 II低）
                satisfyQueue.add(new int[]{roomId, 1000002});  //类似默认空值，但参与排序（按照开始时间），但优先级很低
            } else { //内
                for (int i = 1; i < nums; i++) {  //能否插入有后续会议的会议室
                    //1、有后续会议的会议室（优先级 I高）
                    if (meetings.get(i - 1).endTime <= startTime && endTime <= meetings.get(i).startTime) {
                        satisfyQueue.add(new int[]{roomId, meetings.get(i).startTime});
                        break;
                    }
                }
            }
        }

        //在循环外，如果所有会议室中均有会议的情况下，需要在循环体外处理
        if (!satisfyQueue.isEmpty()) {  //当前有可插入的会议室，且满足插入及其最值条件
            return satisfyQueue.peek()[0];
        }
        return -1;  //无满足条件的会议室
    }

    private static void addMeeting(PriorityQueue<Room> sortedRooms, int roomID, Meeting meet) {
        for (Room room : sortedRooms) {
            if (room.id == roomID) {
                ArrayList<Meeting> meetings = room.meetings;
                meetings.add(meet);
                meetings.sort((o1, o2) -> o1.startTime - o2.startTime);  //按照开始时间升序排序（一个会议室中的会议时间不会冲突）
                room.meetings = meetings;
                break;
            }
        }
    }

    private static void cancelMeeting(PriorityQueue<Room> sortedRooms, int roomID, int meetingID) {
        for (Room room : sortedRooms) {
            if (room.id == roomID) {
                ArrayList<Meeting> meetings = room.meetings;
                for (int i = 0; i < meetings.size(); i++) {
                    if (meetings.get(i).id == meetingID) {  //通过索引获取会议ID，在判断
                        meetings.remove(i);  //无需重新排序
                        break;
                    }
                }
            }
        }
    }

    private static void updateMeeting(PriorityQueue<Room> sortedRooms, int roomID, int meetingID, int startTime, int endTime) {
        for (Room room : sortedRooms) {
            if (room.id == roomID) {
                ArrayList<Meeting> meetings = room.meetings;
                for (int i = 0; i < meetings.size(); i++) {
                    if (meetings.get(i).id == meetingID) {  //更新此会议的开始和结束时间
                        meetings.get(i).startTime = startTime;
                        meetings.get(i).endTime = endTime;
                        meetings.sort((o1, o2) -> o1.startTime - o2.startTime);
                        break;
                    }
                }
            }
        }
    }

    public static class Meeting {
        int id;
        int startTime;
        int endTime;

        Meeting(int id, int startTime, int endTime) {
            this.id = id;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }


    public static class Room {
        int id;
        ArrayList<Meeting> meetings;

        Room(int id, ArrayList<Meeting> meetings) {
            this.id = id;
            this.meetings = meetings;
        }
    }
}
