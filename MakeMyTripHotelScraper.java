package com.eos.hotels.scrapers.makemytrip;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;

import com.eos.b2c.data.LocationData;
import com.eos.hotels.data.HotelSearchQuery;
import com.eos.hotels.gds.data.HotelSearchInformation;
import com.eos.marketplace.data.GenericHotel;
import com.eos.marketplace.data.MarketPlaceHotel.Amenities;

public class MakeMyTripHotelScraper {

    private static final Category  s_logger                       = Category.getInstance(MakeMyTripHotelScraper.class);

    public HotelSearchQuery        m_searchQuery                  = null;
    public String                  m_pageSource                   = "";
    public String                  m_redirectURI;
    protected HttpClient           client;
    protected static final Long    CONNECTION_TIMEOUT_LONG        = new Long(1000 * 60 * 10);                                                                                                                                                                                                                                                                         // 10
    // mins
    protected static final Integer CONNECTION_TIMEOUT_INTEGER     = new Integer(1000 * 60 * 10);                                                                                                                                                                                                                                                                      // 10
    // mins
    private static int             hotelStartID                   = 10000;
    private int                    ratePlanCounter                = 10000;
    private int                    hotelRoomCounter               = 10000;
    private SimpleDateFormat       timseStampFormatter            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static String          HOTEL_INSERT_QUERY_TEMPLATE    = "insert into hotel(id,name,city_id,locality_id,checkin_time,checkout_time,star_rating,description,type,min_price,max_price,summary,image_url,is_enabled,chain_id,supplier_id,supplier_name,create_ts,is_on_request_enabled,time_sla,comm_type,convert_non_avail,comm_rate,base_currency) values(";
    private static String          ROOM_INSERT_QUERY_TEMPLATE     = "insert into hotel_room (id,hotel_id,name,base_adult,is_enabled,is_package,create_ts,description,max_guest,pkg_nights,room_type,default_rate_plans,comm_rate,base_people) values(";
    private static String          RATEPLAN_INSERT_QUERY_TEMPLATE = "insert into hotel_rateplan(id,hotel_id,room_id,name,rate_plan,currency,description) values(";
    private static String          CITY_INSERT_QUERY_TEMPLATE     = "update citylist set mmt_code=";

    private String[]               cityNameList                   = { "Agartala", "Agra", "Ahmedabad", "Aizwal",
            "Ajmer", "Alibagh", "Allahabad", "Allepey", "Almora", "Alwar", "Amla", "Amritsar", "Anand", "Auli",
            "Aurangabad", "Badrinath", "Balrampur", "Bandhavgarh", "Bandipur", "Banerghatta", "Bangalore", "Barailley",
            "Behror", "Belur", "Bhadra", "Bhandardara", "Bharatpur", "Bheemeshwari & Galibore", "Bhilai", "Bhilwara",
            "Bhimtal", "Bhopal", "Bhubaneshwar", "Bikaner", "Binsar", "Bodhgaya", "Calicut", "Cannanore", "Chail",
            "Chalsa", "Chamba", "ChambaHimachal", "Chambal", "Chandigarh", "Chennai", "Chettinad", "Chikmangalur",
            "Chintpurni", "Chiplun", "Chitrakoot", "Chittorgarh", "Cochin", "Coimbatore", "Coonoor", "Coorg",
            "Corbett", "Cuttack", "Dalhousie", "Daman", "Dandeli", "Dapoli", "Darjeeling", "Dausa", "Dehradun",
            "Devprayag", "Dharamshala", "Digha", "Dirang", "Diu", "Dungarpur", "Durgapur", "Elappara", "Ernakulum",
            "Gandhidham", "Gandhinagar", "Gangotri", "Gangtok", "Ganpatipule", "Garhmukteshwar", "Gaya", "Goa",
            "Gokarna", "Gorakhpur", "Gulmarg", "Guruvayoor", "Guwahati", "Gwalior", "Halebid", "Hampi", "Hansi",
            "Haridwar", "Hassan", "Hosur", "Hyderabad", "Idduki", "Igatpuri", "Indore", "Jabalpur", "Jaipur",
            "Jaisalmer", "Jalandhar", "Jammu", "Jamnagar", "Jamshedpur", "Jhansi", "Jodhpur", "Jorhat", "Jwalamukhi",
            "Kabini", "Kalimpong", "Kanataal", "Kanchipuram", "Kanha", "Kanpur", "Kanyakumari", "Karwar", "Kasauli",
            "Katra", "Kausani", "Khajjiar", "Khajuraho", "Khimsar", "Kodaikanal", "Kolhapur", "Kolkata", "Kollam",
            "Konkan", "Kosi", "Kota", "Kottayam", "Kovalam and Poovar", "Kuchesar", "Kullu", "Kumarakom", "Kumbalgarh",
            "Kutch", "Lakshadweep", "Lansdowne", "Leh", "Lonavala And Khandala", "Lucknow", "Ludhiana", "Lunagad",
            "Madurai", "Mahabaleshwar", "Mahabalipuram", "Malshej", "Malvan", "Manali", "Mandavi", "Mandawa", "Mandi",
            "Mangalore", "Marari", "Marchula", "Masinagudi", "Matheran", "Mathura", "Mohali", "Moradabad", "MountAbu",
            "Mukteshwar", "Mumbai", "Munnar", "Munsiyari", "MurudJanjira", "Mussoorie", "Mysore", "Nadukani",
            "Nagarholae", "Nagpur", "Nahan", "Nainital", "Nalagarh", "Naldehra", "Nanded", "Narkanda", "Nasik",
            "NewDelhiAndNCR", "Ooty", "Orchha", "Pahalgam", "Palampur", "Pali", "Pallakad", "Panchgani", "Panchmarhi",
            "Pangot", "Panhala", "Panna", "Paradeep", "Parwanoo", "Pataudi", "Patna", "Patnitop", "Pauri", "Pelling",
            "Pench", "Phalodi", "Pinjore", "Pokhran", "Pondicherry", "Poovar", "PortBlair", "Pragpur", "Pune", "Puri",
            "Pushkar", "Puttaparthi", "Raibareilly", "Raipur", "Rajahmundry", "Rajkot", "Rajsamand", "Rameshwaram",
            "Ramgarh Uttaranchal", "Ranakpur", "Ranchi", "Ranikhet", "Ranthambhore", "Ratnagiri", "Rishikesh", "Ropar",
            "Rourkela", "Rumtek", "Salem", "Sapotra", "Saputara", "Sariska", "Sasan Gir", "Sawai Madhopur",
            "Sawantwadi", "Secunderabad", "Sharavanbelgola", "Shekhawati", "Shillong", "Shimla", "Shirdi",
            "Shrivardhan", "Siana", "Silvassa", "Sindhudurg", "Solan", "Sonapani", "Srinagar", "Surat", "Tanjore",
            "Thane", "Thanjavur", "Thekkady", "Thiruvananthapuram", "Thrissur", "Tiruchirapally", "Tirupati",
            "Tirupur", "Thiruvannamalai", "Udaipur", "Udipi", "Ujjain", "Uttarkashi", "Vadodara", "Vagamon",
            "Valankanni", "Vapi", "Varanasi", "Varkala", "Vellore", "Vijaypur", "Vijaywada", "Vishakhapatnam",
            "Wayanad", "Yamnotri", "Yelagiri", "Yercaud"         };
    private String[]               cityCodeList                   = { "IXA", "AGR", "AMD", "AJL", "XAJ", "XGH", "IXD",
            "XLL", "XLR", "XLW", "XAM", "ATQ", "XAN", "XLI", "IXU", "XBA", "XBP", "XBN", "XBD", "XBT", "BLR", "XBY",
            "XBE", "XBU", "XBR", "XHN", "XHR", "XGB", "XBL", "XBW", "XBH", "BHO", "BBI", "XBK", "XIA", "XBG", "XCL",
            "XCC", "XHI", "XCS", "XNC", "XHL", "XCA", "IXC", "MAA", "XCT", "XCH", "XTP", "XHU", "XCK", "XCI", "COK",
            "CJB", "XCN", "XCR", "XCO", "XCU", "XDA", "XDM", "XDN", "XDP", "IXB", "XDD", "XDE", "XDV", "XDH", "XDI",
            "XDR", "DIU", "XDG", "XDU", "XEP", "XRN", "XGN", "XGR", "XGG", "XGA", "XGP", "XGM", "GAY", "GOI", "XGO",
            "GOP", "XGU", "XGV", "GAU", "GWL", "XHB", "XHP", "XHH", "XHA", "XSS", "XHS", "HYD", "XID", "XIG", "IDR",
            "XAB", "JAI", "JSA", "XJL", "IXJ", "XJA", "XJM", "XJH", "JDH", "JRH", "XJW", "XBI", "XKL", "XAL", "XKN",
            "XAH", "KNU", "XKY", "XKW", "XKS", "XKA", "XAU", "XKJ", "HJR", "XKI", "XKO", "KLH", "CCU", "XLM", "XKK",
            "XSI", "XOK", "XTT", "XOV", "XKC", "XUU", "XKU", "XKM", "XTC", "AGX", "XLN", "IXL", "XLK", "LKO", "XLD",
            "XLU", "IXM", "XMH", "XMB", "XME", "XML", "KUU", "XMV", "XMA", "XND", "IXE", "XMR", "XMM", "XMG", "XAT",
            "XAR", "XOH", "XMO", "XMU", "XMK", "BOM", "XMN", "XMI", "XMJ", "XMS", "XMY", "XNU", "XNA", "NAG", "XNH",
            "XNT", "XLG", "XNL", "XBM", "XRK", "XNS", "DEL", "XOO", "XOR", "XPH", "XPL", "XLA", "XPK", "XPN", "XHM",
            "XGT", "XNP", "XNN", "XEE", "XPA", "XAA", "PAT", "XPT", "XPI", "XPE", "XPP", "XPD", "XPJ", "XHK", "XPC",
            "XPO", "IXZ", "XPG", "PNQ", "XPR", "XPU", "XPY", "XRB", "XAP", "XRM", "XRJ", "XRS", "XAE", "XUT", "XAK",
            "IXR", "XNK", "XRA", "XRG", "XRI", "XRO", "XRR", "XRU", "XSE", "XAO", "XSP", "XSA", "XSG", "XSM", "XSW",
            "SEC", "XSB", "XKH", "SHL", "SLV", "XSH", "XHV", "XSN", "XIL", "XSD", "XSL", "XSO", "SXR", "XSU", "XTA",
            "XTE", "XTJ", "XTH", "TRV", "XTR", "TRZ", "TIR", "XTU", "XTN", "UDR", "XUD", "XUJ", "XUK", "BDQ", "XVE",
            "XVL", "XVA", "VNS", "XVR", "XVV", "XVJ", "XVI", "VTZ", "XWA", "XYA", "XYG", "XYE" };

    // private String[] cityNameList = { "Aizwal", "Almora", "Amla",
    // "Balrampur",
    // "Banerghatta", "Belur", "Bhadra", "Bheemeshwari & Galibore", "Bhilai",
    // "Bikaner", "Chalsa", "Chambal",
    // "Chettinad", "Chintpurni", "Chitrakoot", "Daman", "Dapoli", "Digha",
    // "Dirang", "Diu", "Elappara",
    // "Garhmukteshwar", "Gorakhpur", "Gwalior", "Halebid", "Hampi", "Jabalpur",
    // "Jamshedpur", "Jorhat",
    // "Jwalamukhi", "Kabini", "Kanataal", "Kanpur", "Khajuraho", "Konkan",
    // "Kota", "Kuchesar", "Kumarakom",
    // "Lunagad", "Malshej", "Malvan", "Mandavi", "Marari", "Marchula",
    // "Masinagudi", "Mukteshwar", "Munsiyari",
    // "Murud Janjira", "Nadukani", "Nagarholae", "Nahan", "Nanded", "Narkanda",
    // "Pallakad", "Panchmarhi",
    // "Pangot", "Panhala", "Pataudi", "Patna", "Pauri", "Pokhran", "Poovar",
    // "Port Blair", "Puttaparthi",
    // "Raibareilly", "Raipur", "Ranchi", "Rumtek", "Sapotra", "Sawantwadi",
    // "Sharavanbelgola", "Shrivardhan",
    // "Siana", "Sindhudurg", "Sonapani", "Surat", "Thekkady",
    // "Thiruvannamalai", "Vagamon", "Valankanni", "Vapi",
    // "Vijaypur", "Yamnotri", "Yelagiri", "Alibagh", "Allepey", "Bangalore",
    // "Barailley", "Cannanore", "Chennai",
    // "Cochin", "Ernakulum", "Guruvayoor", "Guwahati", "Jalandhar", "Khajjiar",
    // "Kolkata", "Kovalam and Poovar",
    // "Lonavala And Khandala", "Mount Abu", "Mumbai", "Nasik"," New Delhi And
    // NCR", "Pune",
    // "Ramgarh Uttaranchal", "Ranthambhore", "Rourkela", "Salem", "Sasan Gir",
    // "Sawai Madhopur",
    // "Thiruvananthapuram", "Tiruchirapally", "Tirupati", "Udipi", "Vadodara",
    // "Varanasi", "Vijaypur",
    // "Vijaywada", "Vishakhapatnam" };
    // private String[] cityCodeList = { "AJL", "XLR", "XAM", "XBP", "XBT",
    // "XBU", "XBR",
    // "XGB", "XBL", "XBK", "XCS", "XCA", "XCT", "XTP", "XCK", "XDM", "XDP",
    // "XDI", "XDR", "DIU", "XEP", "XGM",
    // "GOP", "GWL", "XHB", "XHP", "XAB", "XJM", "JRH", "XJW", "XBI", "XAL",
    // "KNU", "HJR", "XKK", "XOK", "XKC",
    // "XKU", "XLU", "XME", "XML", "XMV", "XMR", "XMM", "XMG", "XMK", "XMI",
    // "XMJ", "XNU", "XNA", "XNH", "XBM",
    // "XRK", "XPK", "XHM", "XGT", "XNP", "XAA", "PAT", "XPI", "XHK", "XPO",
    // "IXZ", "XPY", "XRB", "XAP", "IXR",
    // "XRU", "XAO", "XSW", "XSB", "XHV", "XSN", "XSD", "XSO", "XSU", "XTH",
    // "XTN", "XVE", "XVL", "XVA", "XVJ",
    // "XYA", "XYG", "XGH", "XLL", "BLR", "XBY", "XCC", "MAA", "COK", "XRN",
    // "XGV", "GAU", "XJL", "XKJ", "CCU",
    // "XOV", "XLK", "XMU", "BOM", "XNS", "DEL", "PNQ", "XUT", "XRA", "XRR",
    // "XSE", "XSG", "XSM", "TRV", "TRZ",
    // "TIR", "XUD", "BDQ", "VNS", "XVJ", "XVI", "VTZ" };
    private int                    cityIDList[]                   = { 1101050, 1101051, 1101052, 1101053, 1101054,
            1101055, 1101056, 1101057, 1101058, 1101059, 1101060, 1101061, 1101062, 1101063, 1101064, 1101065, 1101066,
            1101067, 1101068, 1101069, 1101070, 1101071, 1101072, 1101073, 1101074, 1101075, 1101076, 1101077, 1101078,
            1101079, 1101080, 1101081, 1101082, 1101083, 1101084, 1101085, 1101086, 1101087, 1101088, 1101089, 1101090,
            1101091, 1101092, 1101093, 1101094, 1101095, 1101096, 1101097, 1101098, 1101099, 1101100, 1101101, 1101102,
            1101103, 1101104, 1101105, 1101106, 1101107, 1101108, 1101109, 1101110, 1101111, 1101112, 1101113, 1101114,
            1101115, 1101116, 1101117, 1101118, 1101119, 1101120, 1101121, 1101122, 1101123, 1101124, 1101125, 1101126,
            1101127, 1101128, 1101129, 1101130, 1101131, 1101132, 1101133, 4129, 1100942, 3581, 6003, 3632, 5196, 3637,
            7064, 1100944, 2280, 4788, 1100567, 6903, 1100599, 1100965, 1100576, 4320, 4339, 2624, 4378, 1101028,
            1100998, 4689, 1100748, 1100913, 5058, 3715, 5798, 2188, 3560, 2899, 6586, 1101131, 2200, 2202 };

    private BufferedWriter         out;
    private BufferedWriter         missingCityWriter;

    public MakeMyTripHotelScraper() {
        // TODO Auto-generated constructor stub
        clientInit();
    }

    public MakeMyTripHotelScraper(HotelSearchQuery query) {
        m_searchQuery = query;
    }

    private void clientInit() {
        client = new HttpClient();
        HostConfiguration hc = new HostConfiguration();

        // hc.setProxy("localhost", 8888);
        client.setHostConfiguration(hc);

        client.getParams().setParameter("http.socket.timeout", CONNECTION_TIMEOUT_INTEGER);
        client.getParams().setParameter("http.connection.timeout", CONNECTION_TIMEOUT_INTEGER);
        client.getParams().setParameter("http.connection-manager.timeout", CONNECTION_TIMEOUT_LONG);

        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        client.getParams().setParameter("http.protocol.single-cookie-header", Boolean.TRUE);

    }

    public List<HotelSearchInformation> doOnlineSearch() throws Exception {

        System.out.println("city Name list length " + cityNameList.length);
        System.out.println("City Code list length " + cityCodeList.length);
        System.out.println("City id list length " + cityIDList.length);
        out = new BufferedWriter(new FileWriter("c:\\sqlList.txt"));
        missingCityWriter = new BufferedWriter(new FileWriter("c:\\missingCityList.txt"));

        // for (int i = 0; i < 4; i++) {
        for (int i = 0; i < cityCodeList.length; i++) {
            GetMethod authget = new GetMethod("http://www.makemytrip.com/hotels");
            client.executeMethod(authget);
            m_pageSource = authget.getResponseBodyAsString();
            if (LocationData.getAllCityIDByName(cityNameList[i]) == -1) {
                System.out.println("Missing city " + cityNameList[i]);
                missingCityWriter.write(cityNameList[i] + "\n");
                continue;
            }
            PostMethod authpost = new PostMethod(
                    "http://hotels.makemytrip.co.in/makemytrip/searchHotelProgress.do?residentOfIndia=Y&method=searchHotels");
            authpost.addRequestHeader(new Header("Referer", "http://www.makemytrip.com/hotels/"));
            NameValuePair numberOfBeds = new NameValuePair("numberOfBeds", "4");
            NameValuePair method = new NameValuePair("method", "searchHotels");
            NameValuePair country = new NameValuePair("CountryName", "India");
            NameValuePair countryCode = new NameValuePair("countryCode", "IN");
            NameValuePair tempCityName = new NameValuePair("tempCityName", cityNameList[i]);
            NameValuePair cityCode = new NameValuePair("cityCode", cityCodeList[i]);
            NameValuePair cityName = new NameValuePair("cityName", cityNameList[i]);
            NameValuePair listCities = new NameValuePair("listCities", cityNameList[i]);
            NameValuePair checkinDate = new NameValuePair("checkInDate", "01/05/2009");
            NameValuePair checkOutDate = new NameValuePair("checkOutDate", "02/05/2009");
            NameValuePair numberOfRooms = new NameValuePair("numberOfRooms", "1");
            NameValuePair adults = new NameValuePair("adults", "2");
            NameValuePair children = new NameValuePair("children", "0");
            NameValuePair hotelName = new NameValuePair("HotelName", "Hotel Name contains...");
            NameValuePair startRating = new NameValuePair("StarRating", "All");
            NameValuePair hotelChain1 = new NameValuePair("HotelChain1", "Select");
            NameValuePair hotelChain2 = new NameValuePair("HotelChain1", "Select");
            NameValuePair hotelchain3 = new NameValuePair("HotelChain1", "Select");
            NameValuePair resident = new NameValuePair("residentOfIndia", "Y");
            authpost.setRequestBody(new NameValuePair[] { numberOfBeds, method, country, countryCode, tempCityName,
                    cityCode, cityName, listCities, checkinDate, checkOutDate, numberOfRooms, adults, children,
                    hotelName, startRating, hotelChain1, hotelChain2, hotelchain3, resident });
            client.executeMethod(authpost);
            GetMethod authgetlast = new GetMethod(
                    "http://hotels.makemytrip.co.in/makemytrip/searchHotels.do?residentOfIndia=Y&method=searchHotels");
            client.executeMethod(authgetlast);
            // System.out.println(authpost.getResponseBodyAsString());
            processResponseData(authgetlast.getResponseBodyAsString(), i);
        }
        out.close();
        missingCityWriter.close();
        return null;
    }

    private void processResponseData(String responseData, int cityIndex) throws IOException, ParseException {
        String[] tokens = responseData.split("hotelDetails\\[[0-9]+\\]= new Array()");
        System.out.println("City name " + cityNameList[cityIndex] + " id "
                + LocationData.getAllCityIDByName(cityNameList[cityIndex]) + " No of results " + (tokens.length - 2));
        for (int i = 1; i < tokens.length - 1; i++) {
            try {
                String sql = generateSQLFromData(tokens[i], i - 1, cityIndex);
                if (sql != null) {
                    out.write(sql);
                }
            } catch (Exception e) {
                System.out.println("Error ignored");
            }
        }
    }

    private String generateSQLFromData(String string, int tokenNumber, int cityIndex) throws ParseException,
            IOException {
        StringBuffer buf = new StringBuffer();
        StringBuffer hotelSQL = new StringBuffer(HOTEL_INSERT_QUERY_TEMPLATE);
        // System.out.println(string);
        int length = new String(tokenNumber + "").length() - 1;
        hotelSQL.append(hotelStartID++);
        String s = "hotelDetails[" + tokenNumber + "]['HotelName']=";
        // System.out.println("Hotel String searched " + s + " length " +
        // s.length() + " index " + string.indexOf(s));
        hotelSQL.append(",'"
                + string.substring(string.indexOf(s) + 30 + length,
                        string.indexOf("hotelDetails[" + tokenNumber + "]['HotelPrimImg']=") - 3).replaceAll("'",
                        "&quot;"));
        int cityID = LocationData.getAllCityIDByName(cityNameList[cityIndex]);
        if (cityID == -1) {
            hotelSQL.append("'," + cityIDList[cityIndex]);
        } else {
            hotelSQL.append("'," + cityID);
        }
        hotelSQL.append(",-1");
        SimpleDateFormat dt1 = new SimpleDateFormat("hhaa");
        SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
        String checkinTime = string.substring(string.indexOf("hotelDetails[" + tokenNumber + "]['ChkInTime']='") + 30
                + length, string.indexOf("hotelDetails[" + tokenNumber + "]['ChkOutTime']='") - 3);
        hotelSQL.append(",'" + dt2.format(dt1.parse(checkinTime)));
        String checkoutTime = string.substring(string.indexOf("hotelDetails[" + tokenNumber + "]['ChkOutTime']='") + 31
                + length, string.indexOf("hotelDetails[" + tokenNumber + "]['Longitude']=") - 3);
        hotelSQL.append("','" + dt2.format(dt1.parse(checkoutTime)));
        hotelSQL.append("',"
                + string.substring(string.indexOf("hotelDetails[" + tokenNumber + "]['StarRating']=") + 30 + length,
                        string.indexOf("hotelDetails[" + tokenNumber + "]['HtlPriority']=") - 2));
        // Description commented for now
        // hotelSQL.append("@7@", string.substring(
        // string.indexOf("hotelDetails[" + tokenNumber + "]['ChkInTime']='") +
        // 30+length, string.indexOf("hotelDetails["
        // + tokenNumber + "]['ChkOutTime']=")));
        // hotelSQL.append("@8@", string.substring(
        // string.indexOf("hotelDetails[" + tokenNumber + "]['ChkInTime']='") +
        // 30+length, string.indexOf("hotelDetails["
        // + tokenNumber + "]['ChkOutTime']=")));
        hotelSQL.append(",''");
        hotelSQL.append("," + 1 + "");
        if (string.indexOf("LowestAvgPrice") != -1) {
            hotelSQL.append(","
                    + string.substring(string.indexOf("hotelDetails[" + tokenNumber + "]['LowestAvgPrice']='") + 35
                            + length, string.indexOf("hotelDetails[" + tokenNumber + "]['RoomTypes']") - 3));
        } else {
            hotelSQL.append(",'");
        }
        hotelSQL.append(",10000000");
        if (string.indexOf("HotelDescription") != -1) {
            int startIndex = string.indexOf("hotelDetails[" + tokenNumber + "]['HotelDescription']=") + 37 + length;
            int diff = string.substring(startIndex).indexOf(".");
            if (string.substring(startIndex).indexOf(">>>") < diff) {
                diff = string.substring(startIndex).indexOf(">>");
            }
            hotelSQL.append(",'" + string.substring(startIndex, startIndex + diff).replaceAll("'", "&quot;"));
        } else {
            hotelSQL.append("','");
        }
        if (string.indexOf("HotelPrimImg") != -1) {
            hotelSQL.append("','"
                    + string.substring(string.indexOf("hotelDetails[" + tokenNumber + "]['HotelPrimImg']='") + 33
                            + length, string.indexOf("hotelDetails[" + tokenNumber + "]['Locations']=") - 2));
        } else {
            hotelSQL.append("','");
        }
        hotelSQL.append(", true");
        hotelSQL.append(",-1");
        // hotelSQL.append("@14@", string.substring(
        // string.indexOf("hotelDetails[" + tokenNumber + "]['ChkInTime']='") +
        // 30+length, string.indexOf("hotelDetails["
        // + tokenNumber + "]['ChkOutTime']=")));
        hotelSQL.append(",2222222");
        hotelSQL.append(",'VIA'");
        hotelSQL.append(",'" + timseStampFormatter.format(new Date()) + "");
        hotelSQL.append("',true");
        hotelSQL.append(",-1");
        hotelSQL.append(",'A'");
        hotelSQL.append(",'-'");
        hotelSQL.append(",0");
        hotelSQL.append(",'INR'");
        hotelSQL.append(");");
        hotelSQL.append("\n");
        buf.append(hotelSQL);

        String[] roomsList = string.split("hotelDetails\\[" + tokenNumber + "\\]\\['RoomTypes'\\]\\[[0-9]\\] = new");
        // System.out.println("number of rooms tokens " + roomsList.length);
        for (int j = 1; j < roomsList.length; j++) {
            // System.out.println("Calling");
            buf.append(getSQLForRooms(roomsList[j], j - 1, tokenNumber));
        }
        return buf.toString();
    }

    private String getSQLForRooms(String string, int roomID, int hotelID) {
        StringBuffer buf = new StringBuffer();
        // System.out.println(string);
        // System.out.println("here 0");
        int startindex = 0;
        StringBuffer roomSQL = new StringBuffer(ROOM_INSERT_QUERY_TEMPLATE);
        roomSQL.append(hotelRoomCounter++);
        roomSQL.append("," + (hotelStartID - 1) + "");
        // System.out.println("here 1");
        String s = "hotelDetails[" + hotelID + "]['RoomTypes'][" + roomID + "]['RoomTypeName']";
        // System.out.println("Searched for " + s + " length " + s.length() +"
        // string index" + string.indexOf(s));
        roomSQL.append(",'"
                + string.substring(string.indexOf(s) + s.length() + 4, string.indexOf("hotelDetails[" + hotelID
                        + "]['RoomTypes'][" + roomID + "]['RoomTypeAvgPrice']") - 3));
        // System.out.println("here 2");
        roomSQL.append("',2");
        roomSQL.append(",true");
        // startindex = string.indexOf("hotelDetails[" + hotelID +
        // "]['RoomTypes'][" + roomID
        // + "]['Tariffs'][0]['PackageAvailable']");
        // System.out.println("Start index " + startindex);
        // String packg = string.substring(startindex,
        // string.indexOf("hotelDetails["+hotelID+"]['RoomTypes']["+roomID+
        // "]['Tariffs'][0]['PackageFro"));
        // System.out.println("Start index " + startindex + " packag = " +
        // packg);
        roomSQL.append(",'N'");
        roomSQL.append(",'" + timseStampFormatter.format(new Date()) + "'");
        roomSQL
                .append(",'<RoomDescription><Description></Description><Policies></Policies><Images></Images></RoomDescription>'");
        roomSQL.append(",3");
        // if (packg.equals("N")) {
        roomSQL.append(",0");
        // } else {

        // startindex = string.indexOf("hotelDetails[" + hotelID +
        // "]['RoomTypes'][" + roomID
        // + "]['Tariffs'][0]['PackageNights'] = '");
        // String packgNights = string.substring(startindex, startindex
        // + string.substring(startindex).indexOf("hotelDetails[" + hotelID +
        // "]['RoomTypes']") - 3);
        // System.out.println("Start index " + startindex + " packg nights " +
        // packgNights);
        // roomSQL.append("," + packgNights);
        // }
        roomSQL.append(",'B'");
        roomSQL.append(",'-|INR|" + ratePlanCounter);
        roomSQL.append("',0");
        roomSQL.append(",1");
        roomSQL.append(");");
        roomSQL.append("\n");
        buf.append(roomSQL);
        StringBuffer ratePlan = new StringBuffer(RATEPLAN_INSERT_QUERY_TEMPLATE);
        ratePlan.append(ratePlanCounter++);
        ratePlan.append("," + (hotelStartID - 1) + "");
        ratePlan.append("," + (hotelRoomCounter - 1) + "");
        ratePlan.append(",'Default Plan'");
        // System.out.println("here 3");
        String startStr = "hotelDetails[" + hotelID + "]['RoomTypes'][" + roomID + "]['Tariffs'][0]['AvgPriceNight";
        startindex = string.indexOf(startStr) + startStr.length() + 6;
        String price = string.substring(startindex, startindex
                + string.substring(startindex).indexOf("hotelDetails[" + hotelID + "]['RoomTypes']") - 3);
        ratePlan.append(",'<HotelRoomRatePlan><BasePricePerDay characteristic=\"\" priority=\"1\"><RoomPrice>" + price
                + "</RoomPrice><ExtraAdultBedPrice>" + 0 + "</ExtraAdultBedPrice><ExtraChildBedPrice age=\"0-5\">" + 0
                + "</ExtraChildBedPrice><ExtraChildBedPrice age=\"6-12\">" + 0 + "</ExtraChildBedPrice><Tax>" + 0
                + "</Tax><Commission>0.0</Commission></BasePricePerDay></HotelRoomRatePlan>'");
        // System.out.println("here 4");
        ratePlan.append(",'INR'");
        ratePlan.append(",'BLANK'");
        ratePlan.append(");");
        ratePlan.append("\n");
        buf.append(ratePlan);
        return buf.toString();
    }

    private void generateCitySQLFromData() throws ParseException, IOException {
        StringBuilder citySQL = null;
        // for(int i=0; i< cityNameList.length; i++) {
        // citySQL = new StringBuilder(CITY_INSERT_QUERY_TEMPLATE);
        // citySQL.append("\'").append(cityCodeList[i]).append("\'");
        // citySQL.append(" where city='").append(cityNameList[i]).append("';");
        // System.out.println(citySQL.toString());
        // }
        for (int i = 0; i < cityCodeList.length; i++) {
            citySQL = new StringBuilder(CITY_INSERT_QUERY_TEMPLATE);
            citySQL.append("\'").append(cityCodeList[i]).append("\'");
            citySQL.append(" where id=").append(cityIDList[i]).append(";");
            System.out.println(citySQL.toString());
        }
    }

    public static class MMTHotel {
        private String             name;
        private String             cityCode;
        private String             imageFile;
        private int                rating;
        private String             location;
        private String             description;
        private List<Amenities>    amenities = new ArrayList<Amenities>();
        private List<MMTHotelRoom> rooms     = new ArrayList<MMTHotelRoom>();

        public MMTHotel() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return imageFile;
        }

        public void setImage(String name) {
            this.imageFile = name;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String loc) {
            this.location = loc;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String desc) {
            this.description = desc;
        }

        public List<Amenities> getAmenities() {
            if (amenities == null) {
                amenities = new ArrayList<Amenities>();
            }
            return amenities;
        }

        public void setAmenities(List<Amenities> Aamenities) {
            amenities = Aamenities;
        }

        public List<MMTHotelRoom> getRooms() {
            if (rooms == null) {
                rooms = new ArrayList<MMTHotelRoom>();
            }
            return rooms;
        }

        public void setRooms(List<MMTHotelRoom> rooms) {
            this.rooms = rooms;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public MMTHotelRoom getRoomByType(MMTRoomType roomType) {
            MMTHotelRoom room = null;
            for (MMTHotelRoom hotelRoom : getRooms()) {
                if (hotelRoom.getRoomType() == roomType) {
                    room = hotelRoom;
                }
            }
            if (room == null) {
                room = new MMTHotelRoom();
                room.setRoomType(roomType);
                getRooms().add(room);
            }
            return room;
        }

        public MMTHotelRoom getLowestPriceRoom() {
            MMTHotelRoom minRoom = null;
            if (getRooms().size() == 0) {
                return null;
            } else {
                double minPrice = getRooms().get(0).getPrice();
                minRoom = getRooms().get(0);
                for (MMTHotelRoom room : getRooms()) {
                    if (room.getPrice() < minPrice) {
                        minRoom = room;
                    }
                }
            }
            return minRoom;
        }
    }

    public static class MMTHotelRoom {

        private static final char BREAKFAST = 'B';
        private static final char LUNCH     = 'L';
        private static final char DINNER    = 'D';

        private MMTRoomType       roomType;
        private double            price;
        private List<Character>   foodArrangement;

        public MMTHotelRoom() {

        }

        public MMTRoomType getRoomType() {
            return roomType;
        }

        public void setRoomType(MMTRoomType roomType) {
            this.roomType = roomType;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Character> getFoodArrangement() {
            if (foodArrangement == null) {
                foodArrangement = new ArrayList<Character>();
            }
            return foodArrangement;
        }

        public void setFoodArrangement(List<Character> foodArrangement) {
            this.foodArrangement = foodArrangement;
        }
    }

    public static enum MMTRoomType {
        NONE(0, "None"), DELUXE(1, "Deluxe"), EXECUTIVE(2, "Executive"), SUPER_DELUXE(3, "Super Deluxe");

        private int    value;
        private String description;

        private MMTRoomType(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return this.value;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public List<MMTHotel> getHotelInfoByCity(String cityCodeStr) throws Exception {

        System.out.println("City Code :" + cityCodeStr);
        int index = -1;
        for (int i = 0; i < cityCodeList.length; i++) {
            if (cityCodeList[i].equalsIgnoreCase(cityCodeStr)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return null;
        }
        out = new BufferedWriter(new FileWriter("e:\\hotels\\forDump\\sql\\MMTinsert" + cityNameList[index] + ".sql"));
        BufferedWriter out2 = new BufferedWriter(new FileWriter("e:\\hotels\\forDump\\MMTInfoget" + cityNameList[index]
                + ".txt"));
        BufferedWriter outImage = new BufferedWriter(new FileWriter("e:\\hotels\\forDump\\MMTimage"
                + cityNameList[index] + ".img"));
        GetMethod authget = new GetMethod("http://www.makemytrip.com/hotels");
        client.executeMethod(authget);
        m_pageSource = authget.getResponseBodyAsString();

        out2.write(m_pageSource);

        PostMethod authpost = new PostMethod(
                "http://hotels.makemytrip.co.in/makemytrip/searchHotelProgress.do?residentOfIndia=Y&method=searchHotels");
        authpost.addRequestHeader(new Header("Referer", "http://www.makemytrip.com/hotels/"));
        NameValuePair numberOfBeds = new NameValuePair("numberOfBeds", "4");
        NameValuePair method = new NameValuePair("method", "searchHotels");
        NameValuePair country = new NameValuePair("CountryName", "India");
        NameValuePair countryCode = new NameValuePair("countryCode", "IN");
        NameValuePair tempCityName = new NameValuePair("tempCityName", cityNameList[index]);
        NameValuePair cityCode = new NameValuePair("cityCode", cityCodeList[index]);
        NameValuePair cityName = new NameValuePair("cityName", cityNameList[index]);
        NameValuePair listCities = new NameValuePair("listCities", cityNameList[index]);
        NameValuePair checkinDate = new NameValuePair("checkInDate", "01/06/2009");
        NameValuePair checkOutDate = new NameValuePair("checkOutDate", "02/06/2009");
        NameValuePair numberOfRooms = new NameValuePair("numberOfRooms", "1");
        NameValuePair adults = new NameValuePair("adults", "2");
        NameValuePair children = new NameValuePair("children", "0");
        NameValuePair hotelName = new NameValuePair("HotelName", "Hotel Name contains...");
        NameValuePair startRating = new NameValuePair("StarRating", "All");
        NameValuePair hotelChain1 = new NameValuePair("HotelChain1", "Select");
        NameValuePair hotelChain2 = new NameValuePair("HotelChain1", "Select");
        NameValuePair hotelchain3 = new NameValuePair("HotelChain1", "Select");
        NameValuePair resident = new NameValuePair("residentOfIndia", "Y");
        authpost.setRequestBody(new NameValuePair[] { numberOfBeds, method, country, countryCode, tempCityName,
                cityCode, cityName, listCities, checkinDate, checkOutDate, numberOfRooms, adults, children, hotelName,
                startRating, hotelChain1, hotelChain2, hotelchain3, resident });
        client.executeMethod(authpost);
        GetMethod authgetlast = new GetMethod(
                "http://hotels.makemytrip.co.in/makemytrip/searchHotels.do?residentOfIndia=Y&method=searchHotels");
        client.executeMethod(authgetlast);
        out2.write("Output for Subbu: " + authgetlast.getResponseBodyAsString());
        // System.out.println("Output for subbu:
        // "+authgetlast.getResponseBodyAsString());
        List<MMTHotel> mmtHotels = parseResponseDataForMP(authgetlast.getResponseBodyAsString(), index);
        for (MMTHotel mmtHotel : mmtHotels) {
            if (mmtHotel.getRooms() == null || mmtHotel.getRooms().size() == 0) {
                continue;
            }
            GenericHotel hotel = new GenericHotel();
            hotel.setSearcherType(GenericHotel.MMT_SEARCHER);
            hotel.setCity(cityNameList[index]);
            hotel.setImage(mmtHotel.getImage());
            hotel.setRating(mmtHotel.getRating());
            hotel.setAmenities(mmtHotel.getAmenities());
            hotel.setLocation(mmtHotel.getLocation());
            hotel.setDescription(mmtHotel.getDescription());
            hotel.setName(mmtHotel.getName());
            hotel.setPrice((int) mmtHotel.getLowestPriceRoom().getPrice());
            hotel.setRoomType(mmtHotel.getLowestPriceRoom().getRoomType().getDescription());
            hotel.setBreakFast(mmtHotel.getLowestPriceRoom().getFoodArrangement().contains(MMTHotelRoom.BREAKFAST));
            out.write(hotel.serialize2sql());
            out.newLine();
            outImage.write(mmtHotel.getImage());
            outImage.newLine();
        }
        out.close();
        outImage.close();
        return mmtHotels;
    }

    private List<MMTHotel> parseResponseDataForMP(String responseData, int index) throws Exception {
        responseData = responseData.replaceAll("\n", "");
        String[] statements = responseData.split("\\{*\\}");
        List<MMTHotel> parsedHotels = new ArrayList<MMTHotel>();
        if (statements == null || statements.length == 0) {
            return parsedHotels;
        }

        StringBuilder hotelDetailSb = new StringBuilder();

        for (String statement : statements) {
            if (statement != null && statement.startsWith("{hD[")) {
                hotelDetailSb.append(statement);
            }
        }
        if (hotelDetailSb.length() == 0) {
            return parsedHotels;
        }
        String[] hotelDetailStatements = hotelDetailSb.toString().split(
                "[h][D][\\[][\\d]*[\\]]\\s*[=]\\s*[n][e][w]\\s*[A][r][r][a][y]\\s*[(]\\s*[)][;]*");
        if (hotelDetailStatements == null || hotelDetailStatements.length == 0) {
            return parsedHotels;
        }
        BufferedWriter out2 = new BufferedWriter(new FileWriter("e:\\MMTInfoparse" + cityNameList[index] + ".txt"));
        for (int i = 0; i < hotelDetailStatements.length; i++) {
            if (i == hotelDetailStatements.length - 1) {
                break;
            }
            /*
             * if (i == 0 || i == 1) { out2.write(hotelDetailStatements[i]); }
             */
            String hotelDetailStatement = hotelDetailStatements[i + 1];
            if (i == 0) {
                System.out.println(hotelDetailStatement);
                out2.write(hotelDetailStatement);
            }

            MMTHotel hotel = new MMTHotel();
            hotel.setCityCode(cityCodeList[index]);
            parsedHotels.add(hotel);
            String[] attributes = hotelDetailStatement.split(";");
            for (String attribute : attributes) {
                if (attribute.startsWith("hD[" + i + "]['HN']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.setName(cleanValue(nvPair[1]));
                } else if (attribute.startsWith("hD[" + i + "]['HPI']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.setImage(cleanValue(nvPair[1]));
                } else if (attribute.startsWith("hD[" + i + "]['SR']")) {
                    String[] nvPair = attribute.split("=");
                    if (!((" ".equals(nvPair[1])) || ("".equals(nvPair[1]))))
                        hotel.setRating(Integer.parseInt(nvPair[1]));
                    else
                        hotel.setRating(-1);
                } else if (attribute.startsWith("hD[" + i + "]['A']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.setLocation(cleanValue(nvPair[1]));
                } else if (attribute.startsWith("hD[" + i + "]['hA'][") && attribute.contains("['aN']")) {
                    String[] nvPair = attribute.split("=");
                    String amen = nvPair[1];
                    amen = cleanValue(amen);
                    if (amen.charAt(0) == ' ')
                        amen.replaceAll(" ", "");
                    hotel.getAmenities().add(getAmenity(amen));
                } else if (attribute.startsWith("hD[" + i + "]['HD']")) {
                    String[] nvPair = attribute.split("=");
                    if (nvPair[1].indexOf("<b>Location</b><BR>") > 0)
                        nvPair[1] = nvPair[1].replace("<b>Location</b><BR>", "");
                    if (nvPair[1].lastIndexOf("<div id") > 0)
                        nvPair[1] = nvPair[1].replaceAll("<div id", "...");
                    hotel.setDescription(cleanValue(nvPair[1]));
                } else if (attribute.startsWith("hD[" + i + "]['rT'][0]['rTN']")) {
                    MMTHotelRoom room = new MMTHotelRoom();
                    hotel.getRooms().add(room);
                    room.setRoomType(MMTRoomType.SUPER_DELUXE);
                } else if (attribute.startsWith("hD[" + i + "]['rT'][1]['rTN']")) {
                    MMTHotelRoom room = new MMTHotelRoom();
                    hotel.getRooms().add(room);
                    room.setRoomType(MMTRoomType.EXECUTIVE);

                } else if (attribute.startsWith("hD[" + i + "]['rT'][2]['rTN']")) {
                    MMTHotelRoom room = new MMTHotelRoom();
                    hotel.getRooms().add(room);
                    room.setRoomType(MMTRoomType.DELUXE);
                } else if (attribute.startsWith("hD[" + i + "]['rT'][0]['rTAP']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.getRoomByType(MMTRoomType.SUPER_DELUXE).setPrice(Double.parseDouble(cleanValue(nvPair[1])));
                } else if (attribute.startsWith("hD[" + i + "]['rT'][1]['rTAP']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.getRoomByType(MMTRoomType.EXECUTIVE).setPrice(Double.parseDouble(cleanValue(nvPair[1])));
                } else if (attribute.startsWith("hD[" + i + "]['rT'][2]['rTAP']")) {
                    String[] nvPair = attribute.split("=");
                    hotel.getRoomByType(MMTRoomType.DELUXE).setPrice(Double.parseDouble(cleanValue(nvPair[1])));
                } else if (attribute.startsWith("hD[" + i + "]['rT'][0]['tf'][0]['MP']")) {
                    String[] nvPair = attribute.split("=");
                    if ("Breakfast".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.SUPER_DELUXE).getFoodArrangement().add(MMTHotelRoom.BREAKFAST);
                    } else if ("Lunch".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.SUPER_DELUXE).getFoodArrangement().add(MMTHotelRoom.LUNCH);
                    } else if ("Dinner".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.SUPER_DELUXE).getFoodArrangement().add(MMTHotelRoom.DINNER);
                    }

                } else if (attribute.startsWith("hD[" + i + "]['rT'][1]['tf'][0]['MP']")) {
                    String[] nvPair = attribute.split("=");
                    if ("Breakfast".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.EXECUTIVE).getFoodArrangement().add(MMTHotelRoom.BREAKFAST);
                    } else if ("Lunch".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.EXECUTIVE).getFoodArrangement().add(MMTHotelRoom.LUNCH);
                    } else if ("Dinner".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.EXECUTIVE).getFoodArrangement().add(MMTHotelRoom.DINNER);
                    }

                } else if (attribute.startsWith("hD[" + i + "]['rT'][2]['tf'][0]['MP']")) {
                    String[] nvPair = attribute.split("=");
                    if ("Breakfast".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.DELUXE).getFoodArrangement().add(MMTHotelRoom.BREAKFAST);
                    } else if ("Lunch".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.DELUXE).getFoodArrangement().add(MMTHotelRoom.LUNCH);
                    } else if ("Dinner".equalsIgnoreCase(cleanValue(nvPair[1]))) {
                        hotel.getRoomByType(MMTRoomType.DELUXE).getFoodArrangement().add(MMTHotelRoom.DINNER);
                    }

                }
            }
        }
        return parsedHotels;
    }

    public Amenities getAmenity(String name) {
        if ("1 children's pool".equals(name))
            return Amenities.POOL;
        else if ("24-hr Coffee Shop".equals(name))
            return Amenities.COFFEE_SHOP;
        // else if ("Activity Centre".equals(name))
        // return Amenities.ACTIVITY_CENTRE;
        else if ("Airport/Rlwy Stn Transfer".equals(name))
            return Amenities.AIRPORT_RLWY_STN_TRANSFER;
        else if ("Ayurveda Centre".equals(name))
            return Amenities.AYURVEDA_CENTRE;
        // else if ("Babysitting Service".equals(name))
        // return Amenities.BABYSITTING_SERVICE;
        // else if ("Beauty Salon".equals(name))
        // return Amenities.BEAUTY_SALON;
        // else if ("Boutique".equals(name))
        // return Amenities.BOUTIQUE;
        else if ("Business Centre".equals(name))
            return Amenities.BUSINESS_CENTER;
        else if ("Car Rental Facility".equals(name))
            return Amenities.CAR_RENTAL_FACILITY;
        // else if ("Car parking (Payable to hotel, if
        // applicable)".equals(name))
        // return Amenities.CAR_PARKING_PAYABLE_TO_HOTEL;
        else if ("Coffee Shop".equals(name))
            return Amenities.COFFEE_SHOP;
        else if ("Conference Facilities".equals(name))
            return Amenities.CONFERENCE_FACILITIES;
        else if ("Credit Cards Accepted".equals(name))
            return Amenities.CREDIT_CARDS_ACCEPTED;
        // else if ("Currency Exchange".equals(name))
        // return Amenities.CURRENCY_EXCHANGE;
        // else if ("Disabled facilities".equals(name))
        // return Amenities.DISABLED_ACCESS;
        else if ("Doctor on Call".equals(name))
            return Amenities.DOCTOR_ON_CALL;
        else if ("Dry Cleaning Service".equals(name))
            return Amenities.DRY_CLEANING_SERVICE;
        // else if ("Florist".equals(name))
        // return Amenities.FLORIST;
        else if ("Gymnasium".equals(name))
            return Amenities.GYM;
        else if ("HotWater".equals(name))
            return Amenities.HOTWATER;
        // else if ("Housekeeping Service".equals(name))
        // return Amenities.HOUSEKEEPING;
        // else if ("Indoor Multi Cuisine Restaurant".equals(name))
        // return Amenities.INDOOR_MULTI_CUISINE_RESTAURANT;
        // else if ("Indoor Restaurant".equals(name))
        // return Amenities.INDOOR_RESTAURANT;
        // else if ("Internet Facility".equals(name))
        // return Amenities.HIGH_SPEED_INTERNET;
        // else if ("Jacuzzi".equals(name))
        // return Amenities.JACUZZI;
        else if ("Laundry Service".equals(name))
            return Amenities.LAUNDRY;
        // else if ("Lawns/gardens".equals(name))
        // return Amenities.LAWNS_GARDENS;
        // else if ("Lounge".equals(name))
        // return Amenities.LOUNGE;
        else if ("Outdoor Swimming Pool".equals(name))
            return Amenities.POOL;
        // else if ("Parking Facility".equals(name))
        // return Amenities.PARKING_FACILITY;
        else if ("Power Backup / Generator".equals(name))
            return Amenities.POWER_BACKUP_GENERATOR;
        else if ("Safety Deposit Lockers".equals(name))
            return Amenities.SAFETY_DEPOSIT_LOCKERS;
        else if ("Shop".equals(name))
            return Amenities.SHOPPING;
        else if ("Spa".equals(name))
            return Amenities.SPA_FACILITY;
        else if ("Swimming Pool".equals(name))
            return Amenities.POOL;
        // else if ("Taxi Services".equals(name))
        // return Amenities.TAXI_SERVICES;
        else if ("Travel Desk".equals(name))
            return Amenities.TRAVEL_DESK;
        // else if ("Valet Parking".equals(name))
        // return Amenities.VALET_PARKING;
        return Amenities.CABLE_TV;
    }

    public void saveCityRawData(String cityCodeStr) throws Exception {
        int index = -1;
        for (int i = 0; i < cityCodeList.length; i++) {
            if (cityCodeList[i].equalsIgnoreCase(cityCodeStr)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return;
        }
        System.out.println("START: Saving Data For: " + cityNameList[index]);
        out = new BufferedWriter(new FileWriter("e:\\HotelRawData" + cityNameList[index] + ".txt"));
        BufferedWriter out2 = new BufferedWriter(new FileWriter("e:\\MMTInfo" + cityNameList[index] + ".txt"));
        GetMethod authget = new GetMethod("http://www.makemytrip.com/hotels");
        client.executeMethod(authget);
        m_pageSource = authget.getResponseBodyAsString();

        out2.write(m_pageSource);

        PostMethod authpost = new PostMethod(
                "http://hotels.makemytrip.co.in/makemytrip/searchHotelProgress.do?residentOfIndia=Y&method=searchHotels");
        authpost.addRequestHeader(new Header("Referer", "http://www.makemytrip.com/hotels/"));
        NameValuePair numberOfBeds = new NameValuePair("numberOfBeds", "4");
        NameValuePair method = new NameValuePair("method", "searchHotels");
        NameValuePair country = new NameValuePair("CountryName", "India");
        NameValuePair countryCode = new NameValuePair("countryCode", "IN");
        NameValuePair tempCityName = new NameValuePair("tempCityName", cityNameList[index]);
        NameValuePair cityCode = new NameValuePair("cityCode", cityCodeList[index]);
        NameValuePair cityName = new NameValuePair("cityName", cityNameList[index]);
        NameValuePair listCities = new NameValuePair("listCities", cityNameList[index]);
        NameValuePair checkinDate = new NameValuePair("checkInDate", "01/06/2009");
        NameValuePair checkOutDate = new NameValuePair("checkOutDate", "02/06/2009");
        NameValuePair numberOfRooms = new NameValuePair("numberOfRooms", "1");
        NameValuePair adults = new NameValuePair("adults", "2");
        NameValuePair children = new NameValuePair("children", "0");
        NameValuePair hotelName = new NameValuePair("HotelName", "Hotel Name contains...");
        NameValuePair startRating = new NameValuePair("StarRating", "All");
        NameValuePair hotelChain1 = new NameValuePair("HotelChain1", "Select");
        NameValuePair hotelChain2 = new NameValuePair("HotelChain1", "Select");
        NameValuePair hotelchain3 = new NameValuePair("HotelChain1", "Select");
        NameValuePair resident = new NameValuePair("residentOfIndia", "Y");
        authpost.setRequestBody(new NameValuePair[] { numberOfBeds, method, country, countryCode, tempCityName,
                cityCode, cityName, listCities, checkinDate, checkOutDate, numberOfRooms, adults, children, hotelName,
                startRating, hotelChain1, hotelChain2, hotelchain3, resident });
        client.executeMethod(authpost);
        GetMethod authgetlast = new GetMethod(
                "http://hotels.makemytrip.co.in/makemytrip/searchHotels.do?residentOfIndia=Y&method=searchHotels");
        client.executeMethod(authgetlast);
        out2.write(authgetlast.getResponseBodyAsString());

        out.close();
        System.out.println("END: Saving Data For: " + cityNameList[index]);
    }

    private static String cleanValue(String value) {
        value = value.trim();
        value = value.replaceAll("[\"]", "");
        value = value.replaceAll("[\']", "");
        return value;
    }

    public static void main(String[] args) {
        // test();

    }

    public static void test1() {
        // try {
        // LocationData.initialize();
        // } catch (Exception e1) {
        // System.out.println("error intializing location data " + e1);
        // System.exit(0);
        // }
        /*
         * String[] citiesToScrape = { "DEL", "BLR", "BOM", "MAA", "CCU", "HYD",
         * "AMD", "JAI", "GOI", "XOV", "COK", "XMS", "SXR", "SLV", "KUU", "XNT",
         * "TIR", "XSH", "XAJ", "XPR", "XKA", "XOO", "XHA", "AGR", "PNQ" };
         */

        String[] citiesToScrape = { "IXA", "AGR", "AMD", "AJL", "XAJ", "XGH", "IXD", "XLL", "XLR", "XLW", "XAM", "ATQ",
                "XAN", "XLI", "IXU", "XBA", "XBP", "XBN", "XBD", "XBT", "BLR", "XBY", "XBE", "XBU", "XBR", "XHN",
                "XHR", "XGB", "XBL", "XBW", "XBH", "BHO", "BBI", "XBK", "XIA", "XBG", "XCL", "XCC", "XHI", "XCS",
                "XNC", "XHL", "XCA", "IXC", "MAA", "XCT", "XCH", "XTP", "XHU", "XCK", "XCI", "COK", "CJB", "XCN",
                "XCR", "XCO", "XCU", "XDA", "XDM", "XDN", "XDP", "IXB", "XDD", "XDE", "XDV", "XDH", "XDI", "XDR",
                "DIU", "XDG", "XDU", "XEP", "XRN", "XGN", "XGR", "XGG", "XGA", "XGP", "XGM", "GAY", "GOI", "XGO",
                "GOP", "XGU", "XGV", "GAU", "GWL", "XHB", "XHP", "XHH", "XHA", "XSS", "XHS", "HYD", "XID", "XIG",
                "IDR", "XAB", "JAI", "JSA", "XJL", "IXJ", "XJA", "XJM", "XJH", "JDH", "JRH", "XJW", "XBI", "XKL",
                "XAL", "XKN", "XAH", "KNU", "XKY", "XKW", "XKS", "XKA", "XAU", "XKJ", "HJR", "XKI", "XKO", "KLH",
                "CCU", "XLM", "XKK", "XSI", "XOK", "XTT", "XOV", "XKC", "XUU", "XKU", "XKM", "XTC", "AGX", "XLN",
                "IXL", "XLK", "LKO", "XLD", "XLU", "IXM", "XMH", "XMB", "XME", "XML", "KUU", "XMV", "XMA", "XND",
                "IXE", "XMR", "XMM", "XMG", "XAT", "XAR", "XOH", "XMO", "XMU", "XMK", "BOM", "XMN", "XMI", "XMJ",
                "XMS", "XMY", "XNU", "XNA", "NAG", "XNH", "XNT", "XLG", "XNL", "XBM", "XRK", "XNS", "DEL", "XOO",
                "XOR", "XPH", "XPL", "XLA", "XPK", "XPN", "XHM", "XGT", "XNP", "XNN", "XEE", "XPA", "XAA", "PAT",
                "XPT", "XPI", "XPE", "XPP", "XPD", "XPJ", "XHK", "XPC", "XPO", "IXZ", "XPG", "PNQ", "XPR", "XPU",
                "XPY", "XRB", "XAP", "XRM", "XRJ", "XRS", "XAE", "XUT", "XAK", "IXR", "XNK", "XRA", "XRG", "XRI",
                "XRO", "XRR", "XRU", "XSE", "XAO", "XSP", "XSA", "XSG", "XSM", "XSW", "SEC", "XSB", "XKH", "SHL",
                "SLV", "XSH", "XHV", "XSN", "XIL", "XSD", "XSL", "XSO", "SXR", "XSU", "XTA", "XTE", "XTJ", "XTH",
                "TRV", "XTR", "TRZ", "TIR", "XTU", "XTN", "UDR", "XUD", "XUJ", "XUK", "BDQ", "XVE", "XVL", "XVA",
                "VNS", "XVR", "XVV", "XVJ", "XVI", "VTZ", "XWA", "XYA", "XYG", "XYE" };

        // String[] citiesToScrape = { "BLR" };

        MakeMyTripHotelScraper newScrapper = new MakeMyTripHotelScraper();
        try {
            for (String cityCode : citiesToScrape) {
                newScrapper.getHotelInfoByCity(cityCode);
            }

            // newScrapper.generateCitySQLFromData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        String[] citiesToScrape = { "BLR" };
        MakeMyTripHotelScraper newScrapper = new MakeMyTripHotelScraper();
        try {
            for (String cityCode : citiesToScrape) {
                newScrapper.saveCityRawData(cityCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
