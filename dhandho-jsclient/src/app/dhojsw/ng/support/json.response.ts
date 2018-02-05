/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
export class JsonResponse {
    public json: any;

    public responseTime: number = 0;

    public requestTime: number = 0;

    public constructor(requestTime: number, json: any) {
        this.requestTime = requestTime;
        this.json = json;
        this.responseTime = /* currentTimeMillis */Date.now();
    }

    public getTimeCost(): number {
        return this.responseTime - this.requestTime;
    }
}



