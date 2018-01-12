class Selector {
    public value(selectors: string): string {
        return (this.find(selectors) || {})['value'];
    }

    public find(selectors: string): any {
        return document.querySelector(selectors);
    }
}

export default new Selector();