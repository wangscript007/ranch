class Merger {
    public merge<T extends object>(target: T, ...sources: T[]): T {
        for (let i = 0; i < sources.length; i++)
            if (sources[i])
                for (let key in sources[i])
                    target[key] = sources[i][key];

        return target;
    }
}

export default new Merger();