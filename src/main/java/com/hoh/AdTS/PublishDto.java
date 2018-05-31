package com.hoh.AdTS;

import lombok.Data;

/**
 * Created by test on 2018-03-24.
 */
public class PublishDto {


    @Data
    public static class Publish {
        protected PublishType pbsType;

        public Publish() {
            pbsType =   PublishType.AdMob;
        }

        public Publish(PublishType pbsType) {
            this.pbsType = pbsType;
        }
    }

    @Data
    public static class AdTS extends Publish{

        public AdTS() {
            super(PublishType.AdTS);
        }

        String imgUrl;
        String link;
        String title;
        String summary;
        String contents;

    }


    @Data
    public static class AdMob extends Publish{
        public AdMob() {
            super(PublishType.AdMob);
        }
    }
}
