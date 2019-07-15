package com.air.logincomponent.data.bean;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         LoginResponse
 *         </p>
 */

public class LoginResponse extends BaseResponse {
    private static final long serialVersionUID = -5408760757604837914L;
    /**
     * status : 200
     * data : {"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCJdLCJleHBpcmUiOjE1MzEyMDcxMzUzNzYsImV4cCI6MTUzMTIxNzkzNSwiZGVwYXJ0Ijoicm9vdCIsInVzZXJOYW1lIjoiTXIuQUciLCJ1c2VySWQiOiIxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjE2NWZjNDkwLWQ1ZmYtNDM5Ny04ZDY4LTc1YTgxODM1N2IzOSIsInRlbmFudCI6ImFjODhjZWIzODZhYTQyMzFiMDliZjQ3MmNiOTM3YzI0IiwiY2xpZW50X2lkIjoidnVlIn0.GC97SCGtpgfxOhmP22XC1Zn9Enc394BvmUaX0zg6qPeHRLE0hxIDjV7cNTSjcsAvxpmqQlyQ1kSoU0gLFCV48jm780uoFQfutSvIwQnQeqCyxgv0bFt2anNYellKlnqyjgaX_J_YidDv7FN1A3e-1W0osNADMa8QtPx6v_kRNVo","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwidXNlck5hbWUiOiJNci5BRyIsInVzZXJJZCI6IjEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiY2xpZW50X2lkIjoidnVlIiwic2NvcGUiOlsicmVhZCJdLCJleHBpcmUiOjE1MzEyMDcxMzUzNzYsImF0aSI6IjE2NWZjNDkwLWQ1ZmYtNDM5Ny04ZDY4LTc1YTgxODM1N2IzOSIsImV4cCI6MTUzMzc5NTUzNSwiZGVwYXJ0Ijoicm9vdCIsImp0aSI6IjBlYjI0MmU4LTMwMjktNDdiNy05OWI4LTRjMDU3YTc2YzUyOCIsInRlbmFudCI6ImFjODhjZWIzODZhYTQyMzFiMDliZjQ3MmNiOTM3YzI0In0.Mu1x4utlp9558Dt58pr-YmFBXWfnpLin42SQQnQ3W1cBHfo9QQSZ58lsBJdfaDwGr-kBuA6icKqoItqkBcNSF5P6Yt5egBgpRfqwLhvHFCNcH1UMXIcpqebElwEb0PEQ7n2CbanzxkDgdKGqfPGn0kmDcC8yRGdZgd9mXqeEsmU","expires_in":"2017","expire":"2018-07-10 15:18:55","scope":"read","userId":"1","sub":"admin","userName":"Mr.AG","depart":"root","tenant":"ac88ceb386aa4231b09bf472cb937c24","jti":"165fc490-d5ff-4397-8d68-75a818357b39"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * access_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCJdLCJleHBpcmUiOjE1MzEyMDcxMzUzNzYsImV4cCI6MTUzMTIxNzkzNSwiZGVwYXJ0Ijoicm9vdCIsInVzZXJOYW1lIjoiTXIuQUciLCJ1c2VySWQiOiIxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjE2NWZjNDkwLWQ1ZmYtNDM5Ny04ZDY4LTc1YTgxODM1N2IzOSIsInRlbmFudCI6ImFjODhjZWIzODZhYTQyMzFiMDliZjQ3MmNiOTM3YzI0IiwiY2xpZW50X2lkIjoidnVlIn0.GC97SCGtpgfxOhmP22XC1Zn9Enc394BvmUaX0zg6qPeHRLE0hxIDjV7cNTSjcsAvxpmqQlyQ1kSoU0gLFCV48jm780uoFQfutSvIwQnQeqCyxgv0bFt2anNYellKlnqyjgaX_J_YidDv7FN1A3e-1W0osNADMa8QtPx6v_kRNVo
         * token_type : bearer
         * refresh_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfbmFtZSI6ImFkbWluIiwidXNlck5hbWUiOiJNci5BRyIsInVzZXJJZCI6IjEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiY2xpZW50X2lkIjoidnVlIiwic2NvcGUiOlsicmVhZCJdLCJleHBpcmUiOjE1MzEyMDcxMzUzNzYsImF0aSI6IjE2NWZjNDkwLWQ1ZmYtNDM5Ny04ZDY4LTc1YTgxODM1N2IzOSIsImV4cCI6MTUzMzc5NTUzNSwiZGVwYXJ0Ijoicm9vdCIsImp0aSI6IjBlYjI0MmU4LTMwMjktNDdiNy05OWI4LTRjMDU3YTc2YzUyOCIsInRlbmFudCI6ImFjODhjZWIzODZhYTQyMzFiMDliZjQ3MmNiOTM3YzI0In0.Mu1x4utlp9558Dt58pr-YmFBXWfnpLin42SQQnQ3W1cBHfo9QQSZ58lsBJdfaDwGr-kBuA6icKqoItqkBcNSF5P6Yt5egBgpRfqwLhvHFCNcH1UMXIcpqebElwEb0PEQ7n2CbanzxkDgdKGqfPGn0kmDcC8yRGdZgd9mXqeEsmU
         * expires_in : 2017
         * expire : 2018-07-10 15:18:55
         * scope : read
         * userId : 1
         * sub : admin
         * userName : Mr.AG
         * depart : root
         * tenant : ac88ceb386aa4231b09bf472cb937c24
         * jti : 165fc490-d5ff-4397-8d68-75a818357b39
         */

        private String access_token;
        private String token_type;
        private String refresh_token;
        private String expires_in;
        private String expire;
        private String scope;
        private String userId;
        private String sub;
        private String userName;
        private String depart;
        private String tenant;
        private String jti;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccessToken(String access_token) {
            this.access_token = access_token;
        }

        public String getTokenType() {
            return token_type;
        }

        public void setTokenType(String token_type) {
            this.token_type = token_type;
        }

        public String getRefreshToken() {
            return refresh_token;
        }

        public void setRefreshToken(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getExpiresIn() {
            return expires_in;
        }

        public void setExpiresIn(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDepart() {
            return depart;
        }

        public void setDepart(String depart) {
            this.depart = depart;
        }

        public String getTenant() {
            return tenant;
        }

        public void setTenant(String tenant) {
            this.tenant = tenant;
        }

        public String getJti() {
            return jti;
        }

        public void setJti(String jti) {
            this.jti = jti;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "access_token='" + access_token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    ", expire='" + expire + '\'' +
                    ", scope='" + scope + '\'' +
                    ", userId='" + userId + '\'' +
                    ", sub='" + sub + '\'' +
                    ", userName='" + userName + '\'' +
                    ", depart='" + depart + '\'' +
                    ", tenant='" + tenant + '\'' +
                    ", jti='" + jti + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                '}';
    }

}
