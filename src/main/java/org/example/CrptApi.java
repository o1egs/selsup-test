package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private int requestsCounter;
    private long lastRequestTime;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ReentrantLock reentrantLock;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.requestsCounter = 0;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.reentrantLock = new ReentrantLock();
    }

    public void create(Document document, String signature) {
        reentrantLock.lock();
        long now = System.nanoTime();
        long interval = timeUnit.toNanos(1);
        if (now - lastRequestTime < interval && requestsCounter >= requestLimit) {
            try {
                Thread.sleep((interval - (now - lastRequestTime)) / 1_000_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lastRequestTime = System.nanoTime();
            requestsCounter = 0;
        } else if (now - lastRequestTime >= interval) {
            lastRequestTime = now;
            requestsCounter = 0;
        }
        makeRequest(document, signature);
    }

    private void makeRequest(Document document, String signature) {
        try {
            String body = objectMapper.writeValueAsString(document);
            String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "?signature=" + signature))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Document created successfully");
            } else {
                System.out.println("Failed to create document: " + response.body());
            }
            requestsCounter++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static class Document {
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private Date production_date;
        private String production_type;
        private List<Product> products;
        private Date reg_date;
        private String reg_number;

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public Date getProduction_date() {
            return production_date;
        }

        public void setProduction_date(Date production_date) {
            this.production_date = production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public Date getReg_date() {
            return reg_date;
        }

        public void setReg_date(Date reg_date) {
            this.reg_date = reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        public static class Description {
            private String articipantInn;

            public String getArticipantInn() {
                return articipantInn;
            }

            public void setArticipantInn(String articipantInn) {
                this.articipantInn = articipantInn;
            }
        }

        public static class Product {
            private String certificate_document;
            private Date certificate_document_date;
            private String certificate_document_number;
            private String owner_inn;
            private String producer_inn;
            private Date production_date;
            private String tnved_code;
            private String uit_code;
            private String uitu_code;

            public String getCertificate_document() {
                return certificate_document;
            }

            public void setCertificate_document(String certificate_document) {
                this.certificate_document = certificate_document;
            }

            public Date getCertificate_document_date() {
                return certificate_document_date;
            }

            public void setCertificate_document_date(Date certificate_document_date) {
                this.certificate_document_date = certificate_document_date;
            }

            public String getCertificate_document_number() {
                return certificate_document_number;
            }

            public void setCertificate_document_number(String certificate_document_number) {
                this.certificate_document_number = certificate_document_number;
            }

            public String getOwner_inn() {
                return owner_inn;
            }

            public void setOwner_inn(String owner_inn) {
                this.owner_inn = owner_inn;
            }

            public String getProducer_inn() {
                return producer_inn;
            }

            public void setProducer_inn(String producer_inn) {
                this.producer_inn = producer_inn;
            }

            public Date getProduction_date() {
                return production_date;
            }

            public void setProduction_date(Date production_date) {
                this.production_date = production_date;
            }

            public String getTnved_code() {
                return tnved_code;
            }

            public void setTnved_code(String tnved_code) {
                this.tnved_code = tnved_code;
            }

            public String getUit_code() {
                return uit_code;
            }

            public void setUit_code(String uit_code) {
                this.uit_code = uit_code;
            }

            public String getUitu_code() {
                return uitu_code;
            }

            public void setUitu_code(String uitu_code) {
                this.uitu_code = uitu_code;
            }
        }
    }
}
