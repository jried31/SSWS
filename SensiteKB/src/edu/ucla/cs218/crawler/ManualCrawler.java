package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import java.util.regex.Pattern;

import edu.ucla.cs218.sensite.MongoConnector;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

public class ManualCrawler extends WebCrawler {

    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     *
     * @return
     */
    public static void crawl(String s, String phenomenon) throws IOException {
        String content = s.toLowerCase();
            //System.out.println(content);

        LinkedList<HashMap<String, HashMap<String, Integer>>> shinglesStopWords = new LinkedList<HashMap<String, HashMap<String, Integer>>>();
        LinkedList<HashMap<String, HashMap<String, Integer>>> shinglesWOStopWords = new LinkedList<HashMap<String, HashMap<String, Integer>>>();
        if (SPLIT_ON_PUNCTIONATION_MARKS) {
            String[] lines = content.split("[.?!+-]");
            SnowballStemmer stemmer = new SnowballStemmer();  // initialize stopwords
            Stopwords stopwords = new Stopwords();
            stemmer.setStemmer("english");
            
            for (int k = 2; k < 6; k++) {
                HashMap<String, HashMap<String, Integer>> shingleKStopWords = new HashMap<String, HashMap<String, Integer>>();
                HashMap<String, HashMap<String, Integer>> shingleKWOStopWords = new HashMap<String, HashMap<String, Integer>>();
                shinglesStopWords.add(shingleKStopWords);
                shinglesWOStopWords.add(shingleKWOStopWords);

                HashMap<String, Integer> freqCountStopwords = new HashMap<String, Integer>();
                HashMap<String, Integer> freqCountWithoutStopwords = new HashMap<String, Integer>();
                shingleKStopWords.put(phenomenon, freqCountStopwords);
                shingleKWOStopWords.put(phenomenon, freqCountWithoutStopwords);

                for (String line : lines) {
                    line = line.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
                    line = line.replaceAll("\\s+", " ");

                    String words[] = line.split(" ");
                    //Remove stopwords
                    LinkedList<String> sentenceStopWords = new LinkedList<String>(),
                            sentencewithoutStopWords = new LinkedList<String>();
                    for (String word : words) {
                        if (word.length() > 0) {
                            word = stemmer.stem(word);
                            //anding one with and one w/o stopwords
                            if (!stopwords.is(word)) {
                                sentencewithoutStopWords.add(word);
                            }

                            sentenceStopWords.add(word);
                        }
                    }
                    //   System.out.println("List w/o stops: "+sentencewithoutStopWords);

                    //Create the Shingle list without the stop words
                    if (sentencewithoutStopWords.size() >= k) {
                        for (int i = 0; i < sentencewithoutStopWords.size() - (k); i++) {
                            String set = sentencewithoutStopWords.get(i) + "@";
                            for (int j = i + 1; j < i + (k); j++) {
                                set += sentencewithoutStopWords.get(j) + "@";
                            }

                            //Add the set to the Hashmap & count freq
                            Integer count = freqCountWithoutStopwords.get(set);
                            if (count == null) {
                                count = 1;
                            } else {
                                count++;
                            }
                            freqCountWithoutStopwords.put(set, count);
                            //       System.out.println("Set w/o stop: "+ set);
                        }
                    }

                    //System.out.println("List w stops: "+sentenceStopWords);
                    if (sentenceStopWords.size() >= k) {
                        for (int i = 0; i < sentenceStopWords.size() - (k); i++) {
                            String set = sentenceStopWords.get(i) + "@";
                            for (int j = i + 1; j < i + (k); j++) {
                                set += sentenceStopWords.get(j) + "@";
                            }

                            //Add the set to the Hashmap & count freq
                            Integer count = freqCountStopwords.get(set);
                            if (count == null) {
                                count = 1;
                            } else {
                                count++;
                            }
                            freqCountStopwords.put(set, count);

                               //         System.out.println("Set w/Stop: "+ set);
                            //matcher.matchPhenomenonToSensor(line,relations);
                        }
                    }
                }
            }
        }

        //Ready to insert values into the DB
        DB db3 = MongoConnector.getDatabase();
        //Iterate through the Shingle LinkList
        for (int i = 2; i < 6; i++) {
            //Grab the Stopwords for each K
            HashMap<String, HashMap<String, Integer>> shingleKStopWords = shinglesStopWords.get(i - 2);
            HashMap<String, HashMap<String, Integer>> shingleKWOStopWords = shinglesWOStopWords.get(i - 2);
            Set<String> phenomenasStop = shingleKStopWords.keySet();
            Set<String> phenomenasWOStop = shingleKWOStopWords.keySet();

            //Iterate through the Sets ~ Phenomena
            for (String phenomena : phenomenasStop) {
                //Grab Freq Counts for Stopwords
                HashMap<String, Integer> freqCountStopwords = shingleKStopWords.get(phenomena);

                BasicDBObject rowStop = new BasicDBObject("phenomena", phenomena)
                        .append("k", i)
                        .append("stopwords", 0);

                //Iterate through each shingle set
                Set<String> shingleStop = freqCountStopwords.keySet();

                if (shingleStop.isEmpty()) {
                    continue;
                }

                BasicDBList relationship = new BasicDBList();
                for (String set : shingleStop) {

                    //jaccardSimilarity(set,i,0,phenomena);
                    relationship.add(new BasicDBObject("set", set).append("count", freqCountStopwords.get(set)));
                    //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                }

                rowStop.append("sets", relationship);
                //System.out.println("Set With Stopwords:\n"+rowStop);
                db3.getCollection("ManualShingle").insert(rowStop);
            }

            //Iterate through the Sets ~ Phenomena without stopwords
            for (String phenomena : phenomenasWOStop) {
                //Grab Freq Counts for vector w/o Stopwords
                HashMap<String, Integer> freqCountWithoutStopwords = shingleKWOStopWords.get(phenomena);

                BasicDBObject rowWOStop = new BasicDBObject("phenomena", phenomena)
                        .append("k", i)
                        .append("stopwords", 1);

                //Iterate through each shingle set
                Set<String> shingleWOStop = freqCountWithoutStopwords.keySet();

                //Loop over for sets that are empty
                if (shingleWOStop.isEmpty()) {
                    continue;
                }

                BasicDBList relationshipWOStop = new BasicDBList();
                for (String set : shingleWOStop) {
                           //  jaccardSimilarity(set,i,1,phenomena);

                    relationshipWOStop.add(new BasicDBObject("set", set).append("count", freqCountWithoutStopwords.get(set)));
                    //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                }

                rowWOStop.append("sets", relationshipWOStop);
                //System.out.println("Set Without Stopwords:\n"+relationshipWOStop);
                db3.getCollection("ManualShingle").insert(rowWOStop);
            }
        }
    }

    public static void main(String args[]) throws IOException {
        crawl("A position sensor is any device that permits position measurement. It can either be an absolute position sensor or a relative one (displacement sensor) .Position sensors can be linear, angular, or multi-axis. Capacitive displacement sensors “are non-contact devices capable of high-resolution measurement of the position and/or change of position of any conductive target”. One of the more common applications of capacitive sensors is for precision positioning. Capacitive displacement sensors can be used to measure the position of objects down to the nanometer level. Ultrasonic sensors (also known as transceivers when they both send and receive, but more generally called transducers) work on a principle similar to radar or sonar which evaluate attributes of a target by interpreting the echoes from radio or sound waves respectively. Ultrasonic sensors can be used to solve even the most complex tasks involving object detection or level measurment with millimeter precision, because their measuring method works reliably under almost all conditions. The Global Positioning System (GPS) is a space-based satellite navigation system that provides location and time information in all weather conditions, anywhere on or near the Earth where there is an unobstructed line of sight to four or more GPS satellites. The Russian Global Navigation Satellite System (GLONASS) was developed contemporaneously with GPS. There are also the planned European Union Galileo positioning system, India's Indian Regional Navigational Satellite System and Chinese Compass navigation system. A GPS receiver calculates its position by precisely timing the signals sent by GPS satellites high above the Earth.", "location");
        crawl("Tunable diode laser absorption spectroscopy (TDLAS) is a technique for measuring the concentration of certain species such as methane, water vapor and many more, in a gaseous mixture using tunable diode lasers and laser absorption spectrometry. A basic TDLAS setup consists of tunable diode laser light source, transmitting (i.e. beam shaping) optics, optically accessible absorbing medium, receiving optics and detectors. When electromagnetic radiation travels through the atmosphere, it may be absorbed or scattered by the constituent particles of the atmosphere. A spectrometer (spectrophotometer, spectrograph or spectroscope) is an instrument used to measure properties of light over a specific portion of the electromagnetic spectrum, typically used in spectroscopic analysis to identify materials. Early spectroscopes were simply prisms with graduations marking wavelengths of light. Modern spectroscopes generally use a diffraction grating, a movable slit, and some kind of photodetector, all automated and controlled by a computer. The basic function of a spectrometer is to take in light, break it into its spectral components, digitize the signal as a function of wavelength, and read it out and display it through a computer. A spectrum analyzer measures the magnitude of an input signal versus frequency within the full frequency range of the instrument. We have developed the AQ6375 grating-based desktop optical spectrum analyzer, which can measure an optical spectrum over a wide wavelength range from 1.2 to 2.4 μm with high wavelength resolution at high speed.", "absorption");
        crawl("A rain sensor or rain switch is a switching device activated by rainfall. There are two main applications for rain sensors. The first is a water conservation device connected to an automatic irrigation system that causes the system to shut down in the event of rainfall. TRW Inc. uses optical sensors to detect the moisture. The sensor projects infrared light into the windshield at a 45-degree angle. If the glass is dry, most of this light is reflected back into the sensor by the front of the windshield. If water droplets are on the glass, they reflect the light in different directions -- the wetter the glass, the less light makes it back into the sensor. Rain Gauge senses water hitting its outside surface using beams of infrared light. It uses the same sensing principle used in millions of automotive rain sensing windshield wiper controls. Infrared detector module for automotive rain sensor application. Infrared emitting diodes in GaAlAs on GaAlAs double hetero (DH) technology in chip on board (COB) assembly technique perform in conjunction with a light guide an optical sensor for raindrops on wind- shields. Infrared light is radiated from the IR transmitter unit and guided to the windshield via the lens. The light intensity measured by the IR receiver is a measure for the quantity of water on the windshield. The smaller the measured intensity the larger the quantity of water on the windshield. Optical sensors like this, which is about the size of a tennis ball, are installed in many modern cars to automate wipers. The sensor uses a system of infrared beams that detects when drops of rain are on the surface of the device. Each sensor reading corresponds to a specific amount of water, with more frequent readings corresponding to more intense rainfall.", "rain");
        crawl("Capacitive sensors detect anything that is conductive or has a dielectric different from that of air. Capacitive sensors are constructed from many different media, such as copper, Indium tin oxide (ITO) and printed ink. Capacitive sensors are noncontact devices capable of high-resolution measurement of the position and/or change of position of any conductive target. Capacitance is a property that exists between any two conductive surfaces within some reasonable proximity. Compared to other noncontact sensing technologies such as optical, laser, eddy-current, and inductive, high-performance capacitive sensors have some distinct advantages. Capacitance sensors detect a change in capacitance when something or someone approaches or touches the sensor. The technique has been used in industrial applications for many years to measure liquid levels, humidity, and material composition. A basic sensor includes a receiver and a transmitter, each of which consists of metal traces formed on layers of a printed-circuit board (PCB). Capacitance sensors are more reliable than mechanical sensors. Capacitive sensors (capacitance gauges) are the nanometrology system of choice for the most demanding precision positioning, scanning and measurement applications in the nano-world. Capacitive Proximity Sensors can detect nonmetal solids and liquids in addition to standard metal targets. They can even sense the presence of some targets through certain other materials, making them an ideal choice in some applications where other sensing technologies simply will not work.", "capacitance");
        crawl("A pH meter is an electronic device used for measuring the pH (acidity or alkalinity) of a liquid (though special probes are sometimes used to measure the pH of semi-solid substances). A typical pH meter consists of a special measuring probe (a glass electrode) connected to an electronic meter that measures and displays the pH reading. The probe is a key part of a pH meter, it is a rod like structure usually made up of glass. At the bottom of the probe there is a bulb, the bulb is a sensitive part of a probe that contains the sensor. A pH meter is used to acquire the measurement of a substance's pH level. The pH of a substance is a measurement of its acidity or basicity. PH is a standard measurement of a solution's acidity or alkalinity. A baseline, neutral pH is 7. A pH indicator is a halochromic chemical compound that is added in small amounts to a solution so that the pH (acidity or basicity) of the solution can be determined visually. pH indicators are frequently employed in titrations in analytical chemistry and biology to determine the extent of a chemical reaction. Litmus is a weak acid. It has a seriously complicated molecule which we will simplify to HLit. The H is the proton which can be given away to something else. The Lit is the rest of the weak acid molecule. Methyl orange is one of the indicators commonly used in titrations. In an alkaline solution, methyl orange is yellow. Phenolphthalein is another commonly used indicator for titrations, and is another weak acid. Litmus is a water-soluble mixture of different dyes extracted from lichens, especially Roccella tinctoria. It is often absorbed onto filter paper to produce one of the oldest forms of pH indicator, used to test materials for acidity.", "pH");
    }
}
