/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
export class JsonResponse {
    public json: any;

    public responseTime: number;

    public requestTime: number;

    public constructor(requestTime: number, json: any) {
        if(this.json === undefined) this.json = null;
        if(this.responseTime === undefined) this.responseTime = 0;
        if(this.requestTime === undefined) this.requestTime = 0;
        this.requestTime = requestTime;
        this.json = json;
        this.responseTime = /* currentTimeMillis */Date.now();
    }

    public getTimeCost(): number {
        return this.responseTime - this.requestTime;
    }
}
JsonResponse['__class'] = 'app.dhojsw.ng.support.JsonResponse';



