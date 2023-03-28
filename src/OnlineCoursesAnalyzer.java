import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is just a demo for you,
 * please run it on JDK17 (some statements may be not allowed in lower version).
 * This is just a demo, and you can extend and implement functions
 * based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]),
                        info[3], info[4], info[5],
                        Integer.parseInt(info[6]), Integer.parseInt(info[7]),
                        Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]), Integer.parseInt(info[10]),
                        Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]), Double.parseDouble(info[13]),
                        Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]), Double.parseDouble(info[16]),
                        Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]), Double.parseDouble(info[19]),
                        Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public Map<String, Integer> getPtcpCountByInst() {
        return courses.stream()
                .collect(Collectors.groupingBy(c -> c.institution,
                        TreeMap::new, Collectors.summingInt(c -> c.participants)));
    }

    //2
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        return courses.stream()
                .collect(Collectors.groupingBy(c -> c.institution + "-" + c.subject,
                        TreeMap::new, Collectors.summingInt(c -> c.participants)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    //3
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> result = new HashMap<>();

        for (Course course : courses) {
            for (String instructor : course.instructors.split(", ")) {
                if (!result.containsKey(instructor)) {
                    List<List<String>> list = new ArrayList<>();
                    list.add(new ArrayList<>());
                    list.add(new ArrayList<>());
                    result.put(instructor, list);
                }
                if (course.instructors.split(", ").length == 1) {
                    if (!result.get(instructor).get(0).contains(course.title)) {
                        result.get(instructor).get(0).add(course.title);
                    }
                } else {
                    if (!result.get(instructor).get(1).contains(course.title)) {
                        result.get(instructor).get(1).add(course.title);
                    }
                }
            }
        }

        for (String key : result.keySet()) {
            Collections.sort(result.get(key).get(0));
            Collections.sort(result.get(key).get(1));
        }

        return result;
    }


    //4
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public List<String> getCourses(int topK, String by) {
        courses.sort(Comparator.comparing((Function<Course, Double>) course -> {
            if (by.equals("hours")) {
                return course.totalHours;
            } else {
                return (double) course.participants;
            }
        })
                        .reversed()
                        .thenComparing(course -> course.title)
        );
        return courses.stream()
                .map(course -> course.title)
                .distinct()
                .limit(topK)
                .collect(Collectors.toList());
    }

    //5
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public List<String> searchCourses(String courseSubject, double percentAudited,
                                      double totalCourseHours) {
        return courses.stream()
                .filter(course -> course.subject.toLowerCase()
                        .contains(courseSubject.toLowerCase()))
                .filter(course -> course.percentAudited >= percentAudited)
                .filter(course -> course.totalHours <= totalCourseHours)
                .sorted(Comparator.comparing(course -> course.title))
                .map(course -> course.title)
                .distinct()
                .collect(Collectors.toList());
    }

    //6
    /**
     * This is just a demo for you,
     * please run it on JDK17 (some statements may be not allowed in lower version).
     * This is just a demo, and you can extend and implement functions
     * based on this demo, or implement it in a different way.
     */
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        HashMap<String, double[]> hashMap = new HashMap<>();
        HashMap<String, String> num2title = new HashMap<>();
        HashMap<String, Date> num2date = new HashMap<>();
        for (Course course : courses) {
            if (!hashMap.containsKey(course.number)) {
                hashMap.put(course.number, new double[]{course.medianAge,
                        course.percentMale, course.percentDegree, 1});
                num2title.put(course.number, course.title);
                num2date.put(course.number, course.launchDate);
            } else {
                hashMap.get(course.number)[0] += course.medianAge;
                hashMap.get(course.number)[1] += course.percentMale;
                hashMap.get(course.number)[2] += course.percentDegree;
                hashMap.get(course.number)[3] += 1;
                if (num2date.get(course.number).before(course.launchDate)) {
                    num2date.replace(course.number, course.launchDate);
                    num2title.replace(course.number, course.title);
                }
            }
        }
        for (String number : hashMap.keySet()) {
            hashMap.get(number)[0] /= hashMap.get(number)[3];
            hashMap.get(number)[1] /= hashMap.get(number)[3];
            hashMap.get(number)[2] /= hashMap.get(number)[3];
            hashMap.get(number)[3] = Math.pow((age - hashMap.get(number)[0]), 2)
                    + Math.pow((gender * 100 - hashMap.get(number)[1]), 2)
                    + Math.pow((isBachelorOrHigher * 100 - hashMap.get(number)[2]), 2);
        }
        return num2title.keySet().stream()
                .sorted((n1, n2) -> {
                    if (hashMap.get(n1)[3] != hashMap.get(n2)[3]) {
                        return Double.compare(hashMap.get(n1)[3], hashMap.get(n2)[3]);
                    } else {
                        return num2title.get(n1).compareTo(num2title.get(n2));
                    }
                })
                .map(num2title::get)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }
}

class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;
    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }

    public String getTitle() {
        return title;
    }
}