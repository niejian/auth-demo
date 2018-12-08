function isNull(data){
    if (null == data || '' == data.trim() || typeof (data) == 'undefined') {
        return true;
    }

    return false;
}