class Message {
    private messages: string[] = [];

    public get(key: string): string {
        return this.messages[key] || key;
    }
}

export default new Message();