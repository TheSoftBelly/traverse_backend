package com.example.userauth.model;

import com.google.cloud.Timestamp;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    private String userName;
    private String email;
    private String profilePicture; // 프로필 사진 URL
    private int postCount; // 게시물 수
    private int reportCount; // 신고 수
    @ElementCollection
    private List<String> verify;
    private String bio; // 사용자 bio
    @ElementCollection
    private List<String> followers; // 팔로워 목록
    @ElementCollection
    private List<String> following; // 팔로잉 목록
    private Timestamp createdAt;
    private Timestamp lastLoginAt;
    private String phoneNumber; // 전화번호
    private String location; // 위치
    private String gender; // 성별
    private String birthdate; // 생일
    private String countryCode;
    private String nativeLanguage; // 모국어
    private String preferredLanguage; // 선호 언어
    @ElementCollection
    private List<String> interestKeywords; // 관심 키워드 목록
    //사용사 상태
    private String status;
    private String reason;
    private Integer durationDays;

    // 기본 생성자
    public User() {
    }

    // 전체 생성자
    public User(String userId, String userName,
                String email, String profilePicture,
                int postCount, int reportCount, String bio,
                List<String> followers,
                List<String> following, Timestamp createdAt,
                Timestamp lastLoginAt, String phoneNumber,
                String location, String gender,
                String birthdate, String countryCode,
                String nativeLanguage, String preferredLanguage,
                List<String> interestKeywords,
                String status, String reason, Integer durationDays ) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.postCount = postCount;
        this.reportCount = reportCount;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.gender = gender;
        this.birthdate = birthdate;
        this.countryCode = countryCode;
        this.nativeLanguage = nativeLanguage;
        this.preferredLanguage = preferredLanguage;
        this.interestKeywords = interestKeywords;
        this.status = status;
        this.reason = reason;
        this.durationDays = durationDays;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public List<String> getVerify() {
        return verify;
    }

    public void setVerify(List<String> verify) {
        this.verify = verify;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Timestamp lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public List<String> getInterestKeywords() {
        return interestKeywords;
    }

    public void setInterestKeywords(List<String> interestKeywords) {
        this.interestKeywords = interestKeywords;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
}